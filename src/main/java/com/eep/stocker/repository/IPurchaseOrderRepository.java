package com.eep.stocker.repository;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface IPurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findAll();
    List<PurchaseOrder> findAllBySupplier(Supplier supplier);
    List<PurchaseOrder> findAllByPurchaseOrderDateBetween(Date start, Date end);
}
