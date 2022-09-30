package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.DomainObjectAlreadyExistsException;
import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.supplier.*;
import com.eep.stocker.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;


    /***
     * Retrieves a specific supplier based on it's uid.
     * @param uid - the uid of the supplier
     * @return - a GetSellerResponse which represents the supplier
     */
    @GetMapping("/{uid}")
    public GetSupplierResponse getAllSuppliers(@PathVariable @ValidUUID(message = "Supplier ID needs to be a UUID") String uid) {
        log.info("get: /api/suppliers/{} called", uid);
        Supplier supplier = supplierService.getSupplierFromUid(uid).orElseThrow(()
                -> new SupplierDoesNotExistException(String.format("Supplier with id of %s does not exist", uid)));
        return supplierMapper.getSupplierResponseFromSupplier(supplier);
    }

    /***
     * Retrieves all suppliers in the system
     * @return a GetAllSuppliersResponse which contains all the suppliers in the system
     */
    @GetMapping("/")
    public GetAllSuppliersResponse getAllSuppliers() {
        log.info("get: /api/suppliers/get called");
        return supplierMapper.getAllSupplierResponseFromList(supplierService.getAllSuppliers());
    }

    /***
     * Creates a new supplier
     * @param supplierRequest - CreateSupplierRequest
     * @return CreateSupplierResponse - including id
     */
    @PostMapping(path = "/create")
    public CreateSupplierResponse createSupplier(@RequestBody @Valid CreateSupplierRequest supplierRequest) {
        log.info("post: /api/suppliers/create called");
        if(supplierService.supplierExists(supplierRequest.getSupplierName()))
            throw new DomainObjectAlreadyExistsException("Supplier " + supplierRequest.getSupplierName() + " already exists");
        var supplier = supplierMapper.mapFromCreateRequest(supplierRequest);
        supplier = supplierService.saveSupplier(supplier);
        return supplierMapper.mapToCreateResponse(supplier);
    }

    /***
     * Updates the supplier with the given ID from an UpdateSupplierRequest.  If a supplier is updated
     * with a name that exists for another supplier then a DomainObjectAlreadyExistsException is thrown.
     * @param request - the supplier we are updating
     * @return - the updated supplier
     * @throws DomainObjectAlreadyExistsException
     */
    @PutMapping(path = "/{uid}")
    public UpdateSupplierResponse updateSupplier(@PathVariable @ValidUUID(message = "Supplier ID needs to be a UUID") String uid,
                                                 @RequestBody @Valid UpdateSupplierRequest request) {
        log.info("put: /api/suppliers/ called");
        if(supplierService.supplierExists(request.getSupplierName()))
            throw new DomainObjectAlreadyExistsException("Supplier already exists");
        var supplier = supplierService.getSupplierFromUid(uid).orElseThrow(() -> new SupplierDoesNotExistException("Supplier Does Not Exist"));
        supplierMapper.updateFromUpdateRequest(supplier, request);
        supplier = supplierService.saveSupplier(supplier);
        return supplierMapper.mapToUpdateResponse(supplier);
    }

    /***
     * Delete a supplier
     * @param uid - uid of the supplier
     * @return a copy of the deleted supplier
     */
    @DeleteMapping(path = "/{uid}")
    public DeletedSupplierResponse deleteSupplier(@PathVariable @ValidUUID(message = "Supplier ID needs to be a UUID") String uid) {
        log.info("delete: /api/suppliers/delete/{} called", uid);
        var supplierOpt = supplierService.getSupplierFromUid(uid);
        if(supplierOpt.isPresent()) {
            var supplier = supplierOpt.get();
            var deletedSupplier = supplierService.deleteSupplierById(supplier.getId());
            return supplierMapper.mapToDeleteResponse(deletedSupplier.get());
        }
        else throw new ResourceNotFoundException("Supplier with id of " + uid + " does not exist");
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
