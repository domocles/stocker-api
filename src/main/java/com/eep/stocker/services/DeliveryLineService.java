package com.eep.stocker.services;

import com.eep.stocker.domain.*;
import com.eep.stocker.repository.IDeliveryLineRepository;
import com.google.common.primitives.Doubles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryLineService {
    private final IDeliveryLineRepository deliveryLineRepository;

    public Optional<DeliveryLine> getDeliveryLineById(Long id) {
        log.info("GetDeliveryLineById {} called", id);
        return deliveryLineRepository.findById(id);
    }

    public List<DeliveryLine> getAllDeliveryLines() {
        log.info("GetAllDeliveryLines called");
        return deliveryLineRepository.findAll();
    }

    public List<DeliveryLine> getAllDeliveryLinesForSupplier(Supplier supplier) {
        log.info("GetAllDeliveryLinesForSupplier {}", supplier.getSupplierName());
        return deliveryLineRepository.findAllByPurchaseOrderLine_PurchaseOrder_Supplier(supplier);
    }

    public List<DeliveryLine> getAllDeliveryLinesForStockableProduct(StockableProduct stockableProduct) {
        log.info("GetAllDeliveryLinesForStockableProduct {} called", stockableProduct.getName());
        return deliveryLineRepository.findAllByPurchaseOrderLine_StockableProduct(stockableProduct);
    }

    public List<DeliveryLine> getAllDeliveryLinesForPurchaseOrder(PurchaseOrder purchaseOrder) {
        log.info("GetAllDeliveryLinesForPurchaseOrder {} called", purchaseOrder.getPurchaseOrderReference());
        return deliveryLineRepository.findAllByPurchaseOrderLine_PurchaseOrder(purchaseOrder);
    }

    public DeliveryLine save(DeliveryLine deliveryLine) {
        log.info("SaveDeliveryLine called");
        return deliveryLineRepository.save(deliveryLine);
    }

    public void deleteDeliveryLine(DeliveryLine deliveryLine) {
        log.info("DeleteDeliveryLine called");
        deliveryLineRepository.delete(deliveryLine);
    }

    @Deprecated
    public List<DeliveryLine> getAllDeliveryLinesForDelivery(Long id) {
        log.info("GetAllDeliveryLinesForDelivery called");
        return deliveryLineRepository.findAllByDelivery_Id(id);
    }

    public List<DeliveryLine> getAllDeliveryLinesForDelivery(UUID uid) {
        log.info("GetAllDeliveryLinesForDelivery called");
        return deliveryLineRepository.findAllByDelivery_Uid(uid);
    }

    public Optional<Double> getSumDeliveredForOrderLine(PurchaseOrderLine orderLine) {
        log.info("Get sum delivered for order line called");
        Optional<Double> totalDeliveredOptional = deliveryLineRepository.getSumOfDeliveriesForPurchaseOrderLine(orderLine);
        if(totalDeliveredOptional.isPresent()) {
            Double totalDelivered = Doubles.constrainToRange(totalDeliveredOptional.get(), 0.0, orderLine.getQty());
            return Optional.of(totalDelivered);
        }
        return totalDeliveredOptional;
    }

    /***
     * Finds a delivery line by its unique identifier, if it doesn't exist returns Optional.empty().  Assumes the
     * uid is valid, wrapping in a try-catch block is potentially expensive
     * @param uid - unique identifier of the delivery line
     * @return an {@code Optional} containing the delivery line
     */
    public Optional<DeliveryLine> getDeliveryLineByUid(String uid) {
        log.info("Get Delivery Line by UID called");
        try {
            var uuid = UUID.fromString(uid);
            return deliveryLineRepository.findByUid(uuid);
        } catch(IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
