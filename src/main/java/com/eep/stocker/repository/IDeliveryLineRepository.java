package com.eep.stocker.repository;

import com.eep.stocker.domain.DeliveryLine;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDeliveryLineRepository extends CrudRepository<DeliveryLine, Long> {
    List<DeliveryLine> findAll();

    List<DeliveryLine> findAllByPurchaseOrderLine_PurchaseOrder_Supplier(Supplier supplier);

    List<DeliveryLine> findAllByPurchaseOrderLine_StockableProduct(StockableProduct product);

    List<DeliveryLine> findAllByPurchaseOrderLine_PurchaseOrder(PurchaseOrder purchaseOrder);

    List<DeliveryLine> findAllByDelivery_Id(Long deliveryId);
}
