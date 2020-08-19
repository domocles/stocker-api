package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.ReferenceGeneratorAlreadyExistsException;
import com.eep.stocker.controllers.error.exceptions.ReferenceGeneratorDoesNotExistException;
import com.eep.stocker.domain.ReferenceGenerator;
import com.eep.stocker.services.ReferenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class ReferenceGeneratorController {
    private static final Logger log = LoggerFactory.getLogger(ReferenceGeneratorController.class);

    private ReferenceGeneratorService referenceGeneratorService;

    public ReferenceGeneratorController(ReferenceGeneratorService referenceGeneratorService) {
        this.referenceGeneratorService = referenceGeneratorService;
    }

    @GetMapping("/api/reference/{refgen_name}")
    public String getNewReference(@PathVariable String refgen_name) {
        log.info("get: /api/reference/" + refgen_name + " called");
        Optional<String> nextRefOpt = referenceGeneratorService.getNextReference(refgen_name);
        if(nextRefOpt.isPresent())
            return nextRefOpt.get();
        else {
            throw new ReferenceGeneratorDoesNotExistException("Reference generator " + refgen_name + " does not exist");
        }
    }

    @PostMapping("/api/reference/{refgen_name}/{prefix}")
    public ResponseEntity<Void> createNewReference(@PathVariable String refgen_name, @PathVariable String prefix) {
        log.info("post: /api/reference/" + refgen_name);
        boolean created = referenceGeneratorService.createReferenceGenerator(refgen_name, prefix, 1L);
        if(!created) {
            throw new ReferenceGeneratorAlreadyExistsException("Reference generator " + refgen_name + " already exists");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/reference/" + refgen_name));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
