package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.DomainObjectAlreadyExistsException;
import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
public class SupplierController {
    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

    private SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/api/suppliers/get/{id}")
    public Supplier getAllSuppliers(@PathVariable long id) {
        log.info("get: /api/suppliers/get/" + id + " called");
        return supplierService.getSupplierFromId(id).get();
    }

    @GetMapping("/api/suppliers/get")
    public List<Supplier> getAllSuppliers() {
        log.info("get: /api/suppliers/get called");
        return supplierService.getAllSuppliers();
    }

    @PostMapping(path = "/api/suppliers/create", produces = "application/json", consumes = "application/json")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        log.info("post: /api/suppliers/create called");
        if(supplierService.supplierExists(supplier.getSupplierName()))
            throw new DomainObjectAlreadyExistsException("Supplier " + supplier.getSupplierName() + " already exists");
        return supplierService.saveSupplier(supplier);
    }

    @PutMapping(path = "/api/suppliers/update")
    public Supplier updateSupplier(@RequestBody Supplier supplier) {
        log.info("put: /api/suppliers/put called");
        if(supplier.getId() == 0) {
            //this is not an update operation
            throw new DomainObjectDoesNotExistException("Supplier " + supplier.getId() + " does not exist.  Please supply a valid id");
        } else
            return supplierService.saveSupplier(supplier);
    }

    @DeleteMapping(path = "/api/suppliers/delete/{id}")
    public Supplier deleteSupplier(@PathVariable long id) {
        log.info("delete: /api/suppliers/delete/" + id + " called");
        Optional<Supplier> supplier = supplierService.deleteSupplierById(id);
        if(supplier.isPresent())
            return supplier.get();
        else throw new DomainObjectDoesNotExistException("Supplier with id of " + id + " does not exist");
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
