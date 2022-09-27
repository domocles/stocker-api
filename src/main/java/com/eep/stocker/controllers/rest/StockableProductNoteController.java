package com.eep.stocker.controllers.rest;


import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.dto.stockableproductnote.*;
import com.eep.stocker.services.StockableProductNoteService;
import com.eep.stocker.services.StockableProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/***
 * @author Sam Burns
 * @version 1.0
 * 15/09/2022
 *
 * Rest controller for stockable product notes
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stockable-product-note")
@Slf4j
@Validated
public class StockableProductNoteController {
    private final StockableProductNoteService stockableProductNoteService;
    private final StockableProductService stockableProductService;
    private final StockableProductNoteMapper mapper;

    /***
     * Gets all stockable product notes
     * @return - a {@code GetAllStockableProductNotesResponse} containing all stockable product notes
     */
    @GetMapping("/")
    public GetAllStockableProductNotesResponse getAllStockableProductNotes() {
        log.info("get: /api/stockable-product-note/ called");
        var response = new GetAllStockableProductNotesResponse();
        stockableProductNoteService.get()
                .stream().map(mapper::mapToLowDetailResponse)
                .forEach(response::addNote);
        return response;
    }

    /***
     * Gets a stockable product note by its unique id
     * @param id - unique identifier, {@code String} representation of the UUID
     * @return - a {@code GetStockableProductNoteResponse} containg the stockable product note
     */
    @GetMapping("/{id}")
    public GetStockableProductNoteResponse getStockableProductNoteById(@PathVariable @ValidUUID String id) {
        log.info("get: /api/stockable-product-note/get/{} called", id);
        var note = stockableProductNoteService.getByUid(id)
                .orElseThrow(() -> new DomainObjectDoesNotExistException("StockableProductNote does not exist"));
        return mapper.mapToGetResponse(note);
    }

    /***
     * Get stockable product notes for a stockable product
     * @param id - the unique identifier of the stockable product
     * @return - a {@code GetAllStockableProductNotesByStockableProductResponse} containing all stockable notes for
     * the product
     */
    @GetMapping("/stockable-product/{id}/")
    public GetAllStockableProductNotesByStockableProductResponse getAllNotesForStockableProduct(@PathVariable @ValidUUID String id) {
        log.info("get: /api/stockable-product-note/stockable-product/{} called", id);
        var response = new GetAllStockableProductNotesByStockableProductResponse();
        stockableProductNoteService.getAllNotesForStockableProductUid(id).stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addNote);
        return response;
    }

    /***
     * Create a new stockable product note
     * @param note - the note to create
     * @return - a {@code CreateStockableProductNoteResponse} that contains the new product note
     */
    @PostMapping(path = "")
    public CreateStockableProductNoteResponse saveNote(@Valid @RequestBody CreateStockableProductNoteRequest note) {
        log.info("post: /api/stockable-product-note/create called");
        var stockableProduct = stockableProductService.getStockableProductByUid(note.getStockableProductId())
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product does not exist"));
        var newNote = mapper.mapFromCreateRequest(note, stockableProduct);
        newNote = stockableProductNoteService.saveNote(newNote);
        return mapper.mapToCreateResponse(newNote);
    }

    /***
     * Update a stockable product note
     * @param uid - the unique identifier of the stockable product to update
     * @param request - the update details
     * @return an {@code UpdateStockableProductNoteResponse} containing the updated stockable product note
     */
    @PutMapping(path = "/{uid}")
    public UpdateStockableProductNoteResponse updateNote(@PathVariable @ValidUUID String uid, @Valid @RequestBody UpdateStockableProductNoteRequest request) {
        log.info("post: /api/stockable-product-note/{} called", uid);
        var note = stockableProductNoteService.getByUid(uid)
                .orElseThrow(() -> new DomainObjectDoesNotExistException("Stockable Product Note Does Not Exist"));
        mapper.updateFromUpdateRequest(note, request);
        if(!request.getStockableProductId().equals(note.getStockableProduct().getUid().toString())) {
            var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                    .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist Exception"));
            note.setStockableProduct(stockableProduct);
        }
        note = stockableProductNoteService.saveNote(note);
        return mapper.mapToUpdateResponse(note);
    }
}
