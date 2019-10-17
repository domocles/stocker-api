package com.eep.stocker.repository;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPurchaseOrderLineRepository extends CrudRepository<PurchaseOrderLine, Long> {
    List<PurchaseOrderLine> findAll();
    List<PurchaseOrderLine> findAllByStockableProduct(StockableProduct stockableProduct);

    List<PurchaseOrderLine> findAllByPurchaseOrder(PurchaseOrder purchaseOrder);
}
