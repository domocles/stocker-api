package com.eep.stocker.repository;

import com.eep.stocker.domain.Supplier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ISupplierRepository extends CrudRepository<Supplier, Long> {
    List<Supplier> findAll();

    Optional<Supplier> findFirstBySupplierName(String supplierName);
}
