package com.eep.stocker.repository;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findAll();
    List<PurchaseOrder> findAllBySupplier(Supplier supplier);
    List<PurchaseOrder> findAllByPurchaseOrderDateBetween(LocalDate start, LocalDate end);
    List<PurchaseOrder> findAllBySupplierOrderReference(String reference);

    Optional<PurchaseOrder> findByPurchaseOrderReference(String reference);

    Optional<PurchaseOrder> findByUid(UUID uid);
}
