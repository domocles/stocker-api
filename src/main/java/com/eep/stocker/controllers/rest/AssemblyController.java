package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.AssemblyDoesNotExistException;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.services.AssemblyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assembly")
public class AssemblyController {
    public static final Logger log = LoggerFactory.getLogger(AssemblyController.class);

    private AssemblyService assemblyService;

    public AssemblyController(AssemblyService assemblyService) {
        this.assemblyService = assemblyService;
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

    @PostMapping("/create")
    public Assembly createAssembly(@RequestBody @Valid Assembly assembly) {
        log.info("post: /api/assembly/create called");
        return assemblyService.saveAssembly(assembly).get();
    }

    @PutMapping("/update")
    public Assembly updateAssembly(@RequestBody @Valid Assembly assembly) {
        log.info("put: /api/assembly/put called");
        return assemblyService.saveAssembly(assembly).get();
    }

    @DeleteMapping("/delete/{id}")
    public Assembly deleteAssemblyById(@PathVariable long id) {
        log.info("delete: /api/assembly/delete/{} called", id);
        return assemblyService.deleteAssemblyById(id)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", id)));
    }

    @DeleteMapping("/delete")
    public Assembly deleteAssembly(@RequestBody @Valid  Assembly assembly) {
        log.info("delete: /api/assembly.delete called");
        return assemblyService.deleteAssembly(assembly)
                .orElseThrow(() -> new AssemblyDoesNotExistException(String.format("Assembly with id of %s does not exist", assembly.getId())));
    }
}
