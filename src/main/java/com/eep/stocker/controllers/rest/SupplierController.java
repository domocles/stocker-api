package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.DomainObjectAlreadyExistsException;
import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.supplier.*;
import com.eep.stocker.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;


    /***
     * Retrieves a specific supplier based on it's uid.
     * @param id - the uid of the supplier
     * @return - a GetSellerResponse which represents the supplier
     */
    @GetMapping("/api/suppliers/get/{id}")
    public GetSupplierResponse getAllSuppliers(@PathVariable String id) {
        log.info("get: /api/suppliers/get/{} called", id);
        Supplier supplier = supplierService.getSupplierFromUid(id).orElseThrow(()
                -> new SupplierDoesNotExistException(String.format("Supplier with id of %s does not exist", id)));
        return supplierMapper.getSupplierResponseFromSupplier(supplier);
    }

    /***
     * Retrieves all suppliers in the system
     * @return a GetAllSuppliersResponse which contains all the suppliers in the system
     */
    @GetMapping("/api/suppliers/get")
    public GetAllSuppliersResponse getAllSuppliers() {
        log.info("get: /api/suppliers/get called");
        return supplierMapper.getAllSupplierResponseFromList(supplierService.getAllSuppliers());
    }

    /***
     * Creates a new supplier
     * @param supplierRequest - CreateSupplierRequest
     * @return CreateSupplierResponse - including id
     */
    @PostMapping(path = "/api/suppliers/create", produces = "application/json", consumes = "application/json")
    public CreateSupplierResponse createSupplier(@RequestBody CreateSupplierRequest supplierRequest) {
        log.info("post: /api/suppliers/create called");
        if(supplierService.supplierExists(supplierRequest.getSupplierName()))
            throw new DomainObjectAlreadyExistsException("Supplier " + supplierRequest.getSupplierName() + " already exists");
        var supplier = supplierMapper.createSupplierFromCreateSupplierRequest(supplierRequest);
        supplier = supplierService.saveSupplier(supplier);
        return supplierMapper.createSupplierResponseFromSupplier(supplier);
    }

    /***
     * Updates the supplier from an UpdateSupplierRequest.  If the request has an id then the domain object with that
     * id is updated.  If no id is present then a new domain object is created and returned.  If a supplier is updated
     * with a name that exists for another supplier then a DomainObjectAlreadyExistsException is thrown.
     * @param request - the supplier we are updating
     * @return - the updated supplier
     * @throws DomainObjectAlreadyExistsException
     */
    @PutMapping(path = "/api/suppliers/update")
    public UpdateSupplierResponse updateSupplier(@RequestBody UpdateSupplierRequest request) {
        log.info("put: /api/suppliers/put called");
        //does this supplier exist
        if(request.getId() == null || request.getId().isEmpty()) {
            var newReq = supplierMapper.createSupplierRequestFromUpdateSupplierRequest(request);
            return supplierMapper.updateSupplierResponseFromCreateSupplierResponse(createSupplier(newReq));
        }
        var supplierOpt = supplierService.getSupplierFromUid(request.getId());
        if(supplierOpt.isPresent()) {
            var supplier = supplierOpt.get();
            //check that the supplier doesn't already exist
            if(!supplier.getSupplierName().equals(request.getSupplierName())) {
                if(supplierService.supplierExists(request.getSupplierName()))
                    throw new DomainObjectAlreadyExistsException("Supplier " + request.getSupplierName() + " already exists");
            }
            supplier = supplierMapper.mapFromUpdateSupplierRequest(request);
            supplier = supplierService.saveSupplier(supplier);
            return supplierMapper.updateSupplierResponseFromSupplier(supplier);
        } else
            throw new DomainObjectDoesNotExistException(String.format("Supplier with id of %s does not exist", request.getId()));

    }

    /***
     * Delete a supplier
     * @param id - uid of the supplier
     * @return a copy of the deleted supplier
     */
    @DeleteMapping(path = "/api/suppliers/delete/{id}")
    public DeletedSupplierResponse deleteSupplier(@PathVariable String id) {
        log.info("delete: /api/suppliers/delete/{} called", id);
        var supplierOpt = supplierService.getSupplierFromUid(id);
        if(supplierOpt.isPresent()) {
            var supplier = supplierOpt.get();
            var deletedSupplier = supplierService.deleteSupplierById(supplier.getId());
            return supplierMapper.deletedSupplierResponseFromSupplier(deletedSupplier.get());
        }
        else throw new ResourceNotFoundException("Supplier with id of " + id + " does not exist");
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
