package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.controllers.error.exceptions.RecordNotFoundException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.stockableproduct.*;
import com.eep.stocker.services.StockableProductNoteService;
import com.eep.stocker.services.StockableProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import javax.validation.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * @author Sam Burns
 * @version 1.0
 * 29/09/2022
 *
 * Rest controller for stockable products
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stockable-products")
@Slf4j
@Validated
public class HomeController {
    private final StockableProductService stockableProductService;
    private final StockableProductMapper stockableProductMapper;


    /***
     * Retrieve the StockableProduct with the given id
     * @param id of the stockable product we wish to retrieve
     * @return GetStockableProductResponse
     * @throws RecordNotFoundException if the stockable product does not exist
     */
    @GetMapping("/get/{id}")
    public GetStockableProductResponse getById(@PathVariable @ValidUUID(message = "Stockable Product Id must be a UUID") String id) {
        log.info("get: /api/stockable-products/get/{} called", id);
        Optional<StockableProduct> stockableProductOpt = stockableProductService.getStockableProductByUid(id);
        StockableProduct stockableProduct = stockableProductOpt.orElseThrow(()
                -> new RecordNotFoundException("Stockable Product ID: '" + id + "' does not exist"));
        return stockableProductMapper.stockableProductResponseFromStockableProduct(stockableProduct);
    }

    /***
     * Retrieve all stockable products
     * @return a list of stockable products
     */
    @GetMapping("/get")
    public GetAllStockableProductResponse getAllStockableProducts() {
        log.info("get: /api/stockable-products/get called");
        GetAllStockableProductResponse response = new GetAllStockableProductResponse();
        List<StockableProduct> allProducts = stockableProductService.getAllStockableProducts();
        allProducts.stream()
                .map(stockableProductMapper::stockableProductResponseFromStockableProduct)
                .forEach(response::addGetStockableProductResponse);
        return response;
    }

    /***
     * Retrieve all categories
     * @return a list of categories
     */
    @GetMapping("/categories")
    public GetAllCategoriesResponse getAllCategories() {
        log.info("get: /api/stockable-products/categories called");
        GetAllCategoriesResponse response = new GetAllCategoriesResponse();
        stockableProductService.getAllCategories().forEach(response::addCategory);
        return response;
    }

    /***
     * Create a new stockable product
     * @param createStockableProductRequest
     * @return the persisted stockable product
     */
    @PostMapping(path = "/create")
    public CreateStockableProductResponse createStockableProduct(@Valid @RequestBody CreateStockableProductRequest createStockableProductRequest) {
        log.info("post: /api/stockable-products/create called");
        Optional<StockableProduct> sb = stockableProductService.findStockableProductByMpn(createStockableProductRequest.getMpn());
        if(sb.isPresent()) {
            log.info("MPN already exists: '{}'", createStockableProductRequest.getMpn());
            throw new MpnNotUniqueException(createStockableProductRequest.getMpn() + " already exists");
        }
        StockableProduct stockableProduct = stockableProductMapper.stockableProductFromCreateStockableProductRequest(createStockableProductRequest);
        stockableProduct = stockableProductService.saveStockableProduct(stockableProduct);
        return stockableProductMapper.createStockableProductResponseFromStockableProduct(stockableProduct);
    }

    /***
     * Update a stockable product
     * @param uid - uniqye indentifier of the stockable product
     * @param request - details of the stockable product
     * @return an {@code UpdateStockableProductResponse} containing the updated stockable product
     */
    @PutMapping(path = "/update/{uid}")
    public UpdateStockableProductResponse updateStockableProduct(@PathVariable @ValidUUID(message = "Stockable Product Id must be a UUID") String uid, @Valid @RequestBody UpdateStockableProductRequest request) {
        log.info("put: /api/stockable-products/update called");
        Optional<StockableProduct> sbOpt = stockableProductService.getStockableProductByUid(uid);
        if(sbOpt.isPresent()) {
            StockableProduct sb = sbOpt.get();
            //if the mpn is changing, make sure it doesn't clash
            if(!sb.getMpn().equals(request.getMpn())) {
                Optional<StockableProduct> mpnCheckOpt = stockableProductService.findStockableProductByMpn(request.getMpn());
                if(mpnCheckOpt.isPresent())
                    throw new MpnNotUniqueException("You are updating the mpn on the stockable product to one that" +
                            "already exists");
            }
            stockableProductMapper.updateStockableProductFromDto(request, sb);
            sb = stockableProductService.updateStockableProduct(sb);
            UpdateStockableProductResponse response = stockableProductMapper.updateStockableResponseFromStockableProduct(sb);
            return response;
        } else
            throw new StockableProductDoesNotExistException("Stockable Product does not exist");
    }

    /***
     * Delete a stockable product
     * @param id - the id of the stockable product to delete
     * @return a response as to whether the stockable product has been deleted
     * @throws ResourceNotFoundException - if the id is not found
     */
    @DeleteMapping(path = "/delete/{id}")
    public Response deleteStockableProduct(@PathVariable @ValidUUID(message = "Stockable Product Id must be a UUID") String id) throws ResourceNotFoundException {
        log.info("delete: /api/stockable-products/delete/{} called", id);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByUid(id);
        if(stockableProduct.isPresent()) {
            stockableProductService.deleteStockableProduct(stockableProduct.get());
            return Response.status(204)
                    .entity("Stockable Product is deleted").build();
        } else {
            throw new ResourceNotFoundException(String.format("StockableProduct with id '%s' doesn't exist", id));
        }
    }

    @PostConstruct
    private void initialize() {
        /*StockableProduct flex51x100 = new StockableProduct();
        flex51x100.setName("FX51x101ILOK");
        flex51x100.setMpn("FX24");
        flex51x100.setCategory("Flex");
        flex51x100.setDescription("ilok lined flex, 51mm ID, 100mm long");
        flex51x100.setInStock(100.0D);
        flex51x100.setStockPrice(1.51D);
        flex51x100.getTags().add("Flex");
        flex51x100.getTags().add("ilok");
        flex51x100.setUnits("Flexes");

        try {
            flex51x100 = this.stockableProductService.saveStockableProduct(flex51x100);

            StockableProductNote note = new StockableProductNote();
            note.setNote("This is a note you know");
            note.setStockableProduct(flex51x100);

            StockableProductNote note2 = new StockableProductNote();
            note2.setNote("This is another note");
            note2.setStockableProduct(flex51x100);

            this.stockableProductNoteService.saveNote(note);
            this.stockableProductNoteService.saveNote(note2);
        } catch(MpnNotUniqueException e) {
            log.error(e.getLocalizedMessage());
        }*/
    }

}
