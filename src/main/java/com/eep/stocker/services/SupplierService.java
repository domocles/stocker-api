package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.DomainObjectAlreadyExistsException;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.ISupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierService {
    private static final Logger log = LoggerFactory.getLogger(SupplierService.class);

    private ISupplierRepository supplierRepository;

    public SupplierService(ISupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier saveSupplier(Supplier supplier) {
        return this.supplierRepository.save(supplier);
    }

    public boolean supplierExists(String name) {
        Optional<Supplier> supplier = supplierRepository.findFirstBySupplierName(name);
        return supplier.isPresent();
    }

    public Optional<Supplier> getSupplierFromId(Long id) {
        return supplierRepository.findById(id);
    }

    public Optional<Supplier> getSupplierFromUid(String uid) { return supplierRepository.findByUid(UUID.fromString(uid)); }

    public Optional<Supplier> deleteSupplierById(long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(supplier.isPresent())
            this.supplierRepository.delete(supplier.get());
        return supplier;
    }
}
