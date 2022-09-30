package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.ReferenceGeneratorAlreadyExistsException;
import com.eep.stocker.controllers.error.exceptions.ReferenceGeneratorDoesNotExistException;
import com.eep.stocker.domain.ReferenceGenerator;
import com.eep.stocker.services.ReferenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

/***
 * @author Sam Burns
 * @version 1.0
 * 30/09/2022
 *
 * Rest controller for the reference generator
 */
@RequiredArgsConstructor
@RequestMapping("/api/reference")
@RestController
@Slf4j
@Validated
public class ReferenceGeneratorController {
    private final ReferenceGeneratorService referenceGeneratorService;



    @GetMapping("/{refgen_name}")
    public String getNewReference(@PathVariable String refgen_name) {
        log.info("get: /api/reference/{} called",  refgen_name);
        Optional<String> nextRefOpt = referenceGeneratorService.getNextReference(refgen_name);
        if(nextRefOpt.isPresent())
            return nextRefOpt.get();
        else {
            throw new ReferenceGeneratorDoesNotExistException("Reference generator " + refgen_name + " does not exist");
        }
    }

    @PostMapping("/{refgen_name}/{prefix}")
    public ResponseEntity<Void> createNewReference(@PathVariable String refgen_name, @PathVariable String prefix) {
        log.info("post: /api/reference/{}", refgen_name);
        boolean created = referenceGeneratorService.createReferenceGenerator(refgen_name, prefix, 1L);
        if(!created) {
            throw new ReferenceGeneratorAlreadyExistsException("Reference generator " + refgen_name + " already exists");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/reference/" + refgen_name));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
