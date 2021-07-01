package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.AssemblyDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.AssemblyLineDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.MpnNotFoundException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.AssemblyService;
import com.eep.stocker.services.StockableProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assembly")
public class AssemblyController {
    public static final Logger log = LoggerFactory.getLogger(AssemblyController.class);

    private AssemblyService assemblyService;
    private StockableProductService stockableProductService;

    public AssemblyController(AssemblyService assemblyService, StockableProductService stockableProductService) {
        this.assemblyService = assemblyService;
        this.stockableProductService = stockableProductService;
    }

    @GetMapping("/get")
    public List<Assembly> getAllAssemblies() {
        log.info("get: /api/assembly/get called");
        return assemblyService.getAllAssemblies();
    }

    @GetMapping("/get/{id}")
    public Assembly getAssemblyById(@PathVariable long id) {
        log.info("get: /api/assembly/get/{} called", id);
        return assemblyService.getAssemblyById(id)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", id)));
    }

    @GetMapping("/component/{id}")
    public List<Assembly> getAssemblyForComponent(@PathVariable long id) {
        log.info("get: /api/assembly/component/{} called", id);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);

        return new ArrayList<>(assemblyService.getAllAssembliesByComponent(stockableProduct.orElseThrow(() ->
                new StockableProductDoesNotExistException(String.format("Component with id of %s does not exist", id)))));
    }

    @GetMapping("/mpn/{mpn}")
    public Assembly getAssemblyForMpn(@PathVariable String mpn) {
        log.info("get: /api/assembly/mpn/{} called", mpn);
        Optional<Assembly> assembly = assemblyService.getAssemblyByMpn(mpn);

        return assembly.orElseThrow(() -> new MpnNotFoundException(String.format("Assembly with mpn of %s does not exist", mpn)));
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        log.info("get: /api/assembly/categories called");
        List<String> categories = assemblyService.getAllCategories();
        return categories;
    }

    @PostMapping("/create")
    public Assembly createAssembly(@RequestBody @Valid Assembly assembly) {
        log.info("post: /api/assembly/create called");
        return assemblyService.saveAssembly(assembly).get();
    }

    @PutMapping("/update")
    public Assembly updateAssembly(@RequestBody @Valid Assembly assembly) {
        log.info("put: /api/assembly/update called");
        return assemblyService.updateAssembly(assembly).get();
    }

    @PutMapping("/addsubassembly/{assemblyId}/{subassemblyId}")
    public Assembly addSubAssembly(@PathVariable long assemblyId, @PathVariable long subassemblyId) {
        log.info("put: /api/assembly/addsubassembly/{}/{} called", assemblyId, subassemblyId);
        Optional<Assembly> assembly = assemblyService.addSubAssemblyToAssemblyById(assemblyId, subassemblyId);

        return assembly.orElseThrow(() -> new AssemblyLineDoesNotExistException("Either the assembly or subassembly does not exist"));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAssemblyById(@PathVariable long id) {
        log.info("delete: /api/assembly/delete/{} called", id);
        assemblyService.deleteAssemblyById(id)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", id)));
    }

    @DeleteMapping("/delete")
    public void deleteAssembly(@RequestBody @Valid  Assembly assembly) {
        log.info("delete: /api/assembly.delete called");
        assemblyService.deleteAssembly(assembly)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", assembly.getId())));
    }
}
