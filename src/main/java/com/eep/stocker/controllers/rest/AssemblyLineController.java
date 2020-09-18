package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.AssemblyLineDoesNotExistException;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.services.AssemblyLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assembly-line")
public class AssemblyLineController {
    public static final Logger log = LoggerFactory.getLogger(AssemblyLineController.class);

    private AssemblyLineService assemblyLineService;

    public AssemblyLineController(AssemblyLineService assemblyLineService) {
        this.assemblyLineService = assemblyLineService;
    }

    @GetMapping("/get")
    public List<AssemblyLine> getAlLAssemblyLines() {
        log.info("get: /api/assembly-line/get called");
        return this.assemblyLineService.getAllAssemblyLines();
    }

    @GetMapping("/get/{id}")
    public AssemblyLine getAssemblyLineById(@PathVariable long id) {
        log.info("get: /api/assembly-line/get/{} called", id);
        return this.assemblyLineService.getAssemblyLineById(id)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException(String.format("Assembly line with id of %s does not exist", id)));
    }

    @PostMapping("/create")
    public AssemblyLine createAssemblyLine(@RequestBody @Valid AssemblyLine assemblyLine) {
        log.info("post: /api/assembly-line/create called");
        return this.assemblyLineService.saveAssemblyLine(assemblyLine).get();
    }

    @PutMapping("/update")
    public AssemblyLine updateAssemblyLine(@RequestBody @Valid AssemblyLine assemblyLine) {
        log.info("put: /api/assembly-line/update called");
        return this.assemblyLineService.saveAssemblyLine(assemblyLine).get();
    }

    @DeleteMapping("/delete/{id}")
    public AssemblyLine deleteAssemblyLine(@PathVariable long id) {
        log.info("delete: /api/assembly-line/delete/{} called", id);
        return this.assemblyLineService.deleteAssemblyLineById(id)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException(String.format("Assembly line with id of %s does not exist", id)));
    }

    @DeleteMapping("/delete")
    public AssemblyLine deleteAssemblyLine(@RequestBody @Valid AssemblyLine assemblyLine) {
        log.info("delete: /api/assembly-line/delete called: {}", assemblyLine);
        return this.assemblyLineService.deleteAssemblyLine(assemblyLine)
                .orElseThrow(() -> new AssemblyLineDoesNotExistException(String.format("Assembly line with id of %s does not exist", assemblyLine.getId())));
    }
}
