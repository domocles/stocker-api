package com.eep.stocker.repository;

import com.eep.stocker.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IPurchaseOrderLineRepository extends CrudRepository<PurchaseOrderLine, Long> {
    List<PurchaseOrderLine> findAll();
    List<PurchaseOrderLine> findAllByStockableProduct(StockableProduct stockableProduct);
    List<PurchaseOrderLine> findAllByPurchaseOrder(PurchaseOrder purchaseOrder);
    List<PurchaseOrderLine> findAllByStockableProductAndAndStatus(StockableProduct stockableProduct, Status status);

    @Query("SELECT SUM(s.qty) FROM purchaseorderline s WHERE s.stockableProduct = :stockableProduct AND s.status <> 'CANCELLED'")
    Optional<Double> getSumOfOrderLinesForStockableProduct(StockableProduct stockableProduct);

    Optional<PurchaseOrderLine> findByUid(String uid);
}
