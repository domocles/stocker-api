package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.AssemblyDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.AssemblyLineDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.MpnNotFoundException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.assembly.*;
import com.eep.stocker.services.AssemblyService;
import com.eep.stocker.services.StockableProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/***
 * @author Sam Burns
 * @version 1.0
 * 24/09/2022
 *
 * Rest controller for an Assembly
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/assembly")
@Slf4j
@Validated
public class AssemblyController {
    private final AssemblyService assemblyService;
    private final AssemblyMapper mapper;
    private final StockableProductService stockableProductService;

    /***
     * Gets all assemblies
     * @return a {@code GetAllAssembliesResponse} containing all assemblies
     */
    @GetMapping("/")
    public GetAllAssembliesResponse getAllAssemblies() {
        log.info("get: /api/assembly/ called");
        var response = new GetAllAssembliesResponse();
        assemblyService.getAllAssemblies().stream()
                .map(mapper::mapToGetLowDetailResponse)
                .forEach(response::addAssembly);
        return response;
    }

    /***
     * Get assembly by its unique id
     * @param uid - the unique id of the assembly
     * @return - a {@code GetAssemblyResponse} containing the assembly
     */
    @GetMapping("/{uid}")
    public GetAssemblyResponse getAssemblyByUid(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/assembly/{} called", uid);
        var assembly = assemblyService.getAssemblyByUid(uid)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", uid)));
        return mapper.mapToGetResponse(assembly);
    }

    /***
     * Get all assemblies by component
     * @param uid - the unique id of the component
     * @return - a {@code GetAssembliesByComponentResponse} containing all assemblies that contain a component
     */
    @GetMapping("/component/{uid}")
    public GetAssembliesByComponentResponse getAssemblyForComponent(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/assembly/component/{} called", uid);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByUid(uid);

        var response = new GetAssembliesByComponentResponse();

        var assemblies = assemblyService.getAllAssembliesByComponent(stockableProduct.orElseThrow(() ->
                new StockableProductDoesNotExistException(String.format("Component with id of %s does not exist", uid))));

        assemblies.stream()
                .map(mapper::mapToGetLowDetailResponse)
                .forEach(response::addAssembly);

        return response;
    }

    /***
     * Gets an assembly by its MPN
     * @param mpn - the manufacturers part number
     * @return - a {@code GetAssemblyByMpnResponse} containing the assembly
     */
    @GetMapping("/mpn/{mpn}")
    public GetAssemblyByMpnResponse getAssemblyForMpn(@PathVariable String mpn) {
        log.info("get: /api/assembly/mpn/{} called", mpn);
        var assembly = assemblyService.getAssemblyByMpn(mpn)
                .orElseThrow(() -> new MpnNotFoundException(String.format("Assembly with mpn of %s does not exist", mpn)));

        return mapper.mapToGetByMpnResponse(assembly);
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        log.info("get: /api/assembly/categories called");
        List<String> categories = assemblyService.getAllCategories();
        return categories;
    }

    /***
     * Create a new assembly
     * @param request - the assembly to create
     * @return - a {@code CreateAssemblyResponse} representing the new assembly
     */
    @PostMapping("/")
    public CreateAssemblyResponse createAssembly(@RequestBody @Valid CreateAssemblyRequest request) {
        log.info("post: /api/assembly/create called");
        var assembly = mapper.mapFromCreateRequest(request);
        assembly = assemblyService.saveAssembly(assembly).get();
        return mapper.mapTpCreateResponse(assembly);
    }

    /***
     * Update assembly based on its uid
     * @param uid - the unique identifier of the assembly to update
     * @param request - the details to update the assembly with
     * @return - an {@code UpdateAssemblyResponse} representing the new assembly
     */
    @PutMapping("/{uid}")
    public UpdateAssemblyResponse updateAssembly(@PathVariable @ValidUUID String uid, @RequestBody @Valid UpdateAssemblyRequest request) {
        log.info("put: /api/assembly/{} called", uid);
        var assembly = assemblyService.getAssemblyByUid(uid)
                .orElseThrow(() -> new AssemblyDoesNotExistException("Assembly does not exist"));
        mapper.updateFromUpdateRequest(assembly, request);
        assembly = assemblyService.saveAssembly(assembly).get();
        return mapper.mapToUpdateResponse(assembly);
    }

    /***
     * Add a subassembly to an assembly
     * @param assemblyId - the unique id of the parent assembly
     * @param subassemblyId - the unique id of the sub assembly
     * @return an {@code UpdateAssemblyResponse} containing the updated Assembly
     */
    @PutMapping("/addsubassembly/{assemblyId}/{subassemblyId}")
    public UpdateAssemblyResponse addSubAssembly(@PathVariable @ValidUUID String assemblyId, @PathVariable @ValidUUID String subassemblyId) {
        log.info("put: /api/assembly/addsubassembly/{}/{} called", assemblyId, subassemblyId);
        var assembly = assemblyService.getAssemblyByUid(assemblyId)
                .orElseThrow(() -> new AssemblyDoesNotExistException("Assembly does not exist"));
        var subAssembly = assemblyService.getAssemblyByUid(subassemblyId)
                .orElseThrow(() -> new AssemblyDoesNotExistException("Assembly does not exist"));

        var updatedAssembly = assemblyService.addSubAssemblyToAssemblyById(assembly.getId(), subAssembly.getId())
                .orElseThrow(() -> new AssemblyLineDoesNotExistException("Either the assembly or subassembly does not exist"));

        return mapper.mapToUpdateResponse(updatedAssembly);
    }

    /***
     * Delete an assembly by its unique identifier
     * @param uid - the unique identifier of the assembly
     */
    @DeleteMapping("/{uid}")
    public void deleteAssemblyById(@PathVariable @ValidUUID String uid) {
        log.info("delete: /api/assembly/{} called", uid);
        assemblyService.deleteAssemblyByUid(uid)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", uid)));
    }
}

