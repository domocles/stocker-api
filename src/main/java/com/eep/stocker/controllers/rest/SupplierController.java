package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class SupplierController {
    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

    private SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/api/suppliers/get")
    public List<Supplier> getAllSuppliers() {
        log.info("get: /api/suppliers/get called");
        return supplierService.getAllSuppliers();
    }

    @PostMapping(path = "/api/suppliers/create", produces = "application/json", consumes = "application/json")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        log.info("post: /api/suppliers/create called");
        return supplierService.saveSupplier(supplier);
    }

    @PostConstruct
    public void CreateSomeSuppliers() {
        Supplier ukf = new Supplier();
        ukf.setSupplierName("UKF Ltd");
        ukf.setTelephoneNumber("01527 578686");
        ukf.setDefaultCurrency("GBP");

        supplierService.saveSupplier(ukf);

        Supplier shelleys = new Supplier();
        shelleys.setSupplierName("Shelleys");
        shelleys.setEmailAddress("jon.horton@shelleyflange.com");
        shelleys.setDefaultCurrency("GBP");

        supplierService.saveSupplier(shelleys);
    }
}
