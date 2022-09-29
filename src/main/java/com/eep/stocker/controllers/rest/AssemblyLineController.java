package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.AssemblyDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.AssemblyLineDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.dto.assemblyline.*;
import com.eep.stocker.services.AssemblyLineService;
import com.eep.stocker.services.AssemblyService;
import com.eep.stocker.services.StockableProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/***
 * @author Sam Burns
 * @version 1.0
 * 28/09/2022
 *
 * Rest controller for an Assembly Line
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/assembly-line")
@Slf4j
@Validated
public class AssemblyLineController {
    private final AssemblyService assemblyService;
    private final AssemblyLineService assemblyLineService;
    private final AssemblyLineMapper mapper;
    private final StockableProductService stockableProductService;

    /***
     * Get all assembly lines end point
     * @return a {@code GetAllAssemblyLinesResponse} containing all Assembly Lines
     */
    @GetMapping("/")
    public GetAllAssemblyLinesResponse getAlLAssemblyLines() {
        log.info("get: /api/assembly-line/ called");
        var response = new GetAllAssemblyLinesResponse();
        this.assemblyLineService.getAllAssemblyLines().stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addAssemblyLine);
        return response;
    }

    /***
     * Get an assembly by its unique id
     * @param uid - the unique id of the assembly line to return
     * @return - a {@code GetAssemblyLineResponse} containing the Assembly Line
     */
    @GetMapping("/{uid}")
    public GetAssemblyLineResponse getAssemblyLineById(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/assembly-line/{} called", uid);
        var assemblyLine = assemblyLineService.getAssemblyLineByUid(uid)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException(String.format("Assembly line with id of %s does not exist", uid)));
        return mapper.mapToGetResponse(assemblyLine);
    }

    /***
     * Get all assembly lines for an assembly
     * @param uid - the unique identifier of the assembly to get assembly lines for
     * @return - a {@code GetAllAssemblyLinesResponse} containing all assembly lines for an assembly
     */
    @GetMapping("/assembly/{uid}")
    public GetAllAssemblyLinesResponse getAssemblyLinesForAssembly(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/assembly-line/assembly/{} called", uid);
        var assembly = this.assemblyService.getAssemblyByUid(uid).
                orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", uid)));
        var assemblyLine = this.assemblyLineService.getAllAssemblyLinesForAssembly(assembly);
        var response = new GetAllAssemblyLinesResponse();
        assemblyLine.stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addAssemblyLine);
        return response;
    }

    /***
     * Create a new assembly line
     * @param request - the request that defines the new assembly line
     * @return
     */
    @PostMapping("/")
    public CreateAssemblyLineResponse createAssemblyLine(@RequestBody @Valid CreateAssemblyLineRequest request) {
        log.info("post: /api/assembly-line/ called");
        var assemblyLine = mapper.mapFromCreateRequest(request);
        var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                        .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product does not exist"));
        var assembly = assemblyService.getAssemblyByUid(request.getAssemblyId())
                .orElseThrow(() -> new AssemblyDoesNotExistException("Assembly does not exist"));
        assemblyLine.setAssembly(assembly);
        assemblyLine.setStockableProduct(stockableProduct);

        assemblyLine = assemblyLineService.saveAssemblyLine(assemblyLine).orElseThrow(() -> new AssemblyLineDoesNotExistException("Assembly Line could not be created"));
        return mapper.mapToCreateResponse(assemblyLine);
    }

    /***
     * Update an assembly line
     * @param uid - the unique identifier of the assembly line to update
     * @param request - the details of the update
     * @return - a {@code UpdateLineAssemblyResponse} containing the updated assembly line
     */
    @PutMapping("/{uid}")
    public UpdateAssemblyLineResponse updateAssemblyLine(@PathVariable @ValidUUID @Valid String uid, @RequestBody @Valid UpdateAssemblyLineRequest request) {
        log.info("put: /api/assembly-line/{} called", uid);
        var assemblyLine = assemblyLineService.getAssemblyLineByUid(uid)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException("Assembly Line does not exist"));
        mapper.updateFromUpdateRequest(assemblyLine, request);

        if(!assemblyLine.getAssembly().getUid().toString().equals(request.getAssemblyId())) {
            var assembly = assemblyService.getAssemblyByUid(request.getAssemblyId())
                    .orElseThrow(() -> new AssemblyDoesNotExistException("Assembly does not exist"));
            assemblyLine.setAssembly(assembly);
        }

        if(!assemblyLine.getStockableProduct().getUid().toString().equals(request.getStockableProductId())) {
            var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                    .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable product does not exist"));
            assemblyLine.setStockableProduct(stockableProduct);
        }

        assemblyLine = assemblyLineService.saveAssemblyLine(assemblyLine)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException("Could not create assembly line"));

        return mapper.mapToUpdateResponse(assemblyLine);
    }

    /***
     * Delete an assembly line
     * @param uid - the unique identifier of the assembly line to delete
     * @return - a {@code DeleteAssemblyLineResponse} containing the deleted assembly line
     */
    @DeleteMapping("/{uid}")
    public DeleteAssemblyLineResponse deleteAssemblyLine(@PathVariable @ValidUUID String uid) {
        log.info("delete: /api/assembly-line/delete/{} called", uid);
        var assemblyLine = assemblyLineService.getAssemblyLineByUid(uid)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException(String.format("Assembly line with id of %s does not exist", uid)));
        assemblyLine = assemblyLineService.deleteAssemblyLineById(assemblyLine.getId())
                .orElseThrow(() -> new AssemblyLineDoesNotExistException(String.format("Assembly line with id of %s cannot be deleted", uid)));
        return mapper.mapToDeleteResponse(assemblyLine);
    }
}
