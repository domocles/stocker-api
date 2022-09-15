package com.eep.stocker.repository;

import com.eep.stocker.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDeliveryLineRepository extends CrudRepository<DeliveryLine, Long> {
    List<DeliveryLine> findAll();

    List<DeliveryLine> findAllByPurchaseOrderLine_PurchaseOrder_Supplier(Supplier supplier);

    List<DeliveryLine> findAllByPurchaseOrderLine_StockableProduct(StockableProduct product);

    List<DeliveryLine> findAllByPurchaseOrderLine_PurchaseOrder(PurchaseOrder purchaseOrder);

    List<DeliveryLine> findAllByDelivery_Id(Long deliveryId);

    List<DeliveryLine> findAllByDelivery_Uid(UUID uid);

    @Query("SELECT SUM(s.quantityDelivered) FROM deliveryline s WHERE s.purchaseOrderLine = :poLine")
    Optional<Double> getSumOfDeliveriesForPurchaseOrderLine(PurchaseOrderLine poLine);

    Optional<DeliveryLine> findByUid(UUID uid);
}
