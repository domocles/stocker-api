package com.eep.stocker.services;

import com.eep.stocker.domain.*;
import com.eep.stocker.repository.IDeliveryLineRepository;
import com.google.common.primitives.Doubles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryLineService {
    private static final Logger log = LoggerFactory.getLogger(DeliveryLineService.class);

    private IDeliveryLineRepository deliveryLineRepository;

    @Autowired
    public DeliveryLineService(IDeliveryLineRepository deliveryLineRepository) {
        this.deliveryLineRepository = deliveryLineRepository;
    }

    public Optional<DeliveryLine> getDeliveryLineById(Long id) {
        log.info("GetDeliveryLineById " + id + " called");
        return deliveryLineRepository.findById(id);
    }

    public List<DeliveryLine> getAllDeliveryLines() {
        log.info("GetAllDeliveryLines called");
        return deliveryLineRepository.findAll();
    }

    public List<DeliveryLine> getAllDeliveryLinesForSupplier(Supplier supplier) {
        log.info("GetAllDeliveryLinesForSupplier " + supplier.getSupplierName());
        return deliveryLineRepository.findAllByPurchaseOrderLine_PurchaseOrder_Supplier(supplier);
    }

    public List<DeliveryLine> getAllDeliveryLinesForStockableProduct(StockableProduct stockableProduct) {
        log.info("GetAllDeliveryLinesForStockableProduct " + stockableProduct.getName() + " called");
        return deliveryLineRepository.findAllByPurchaseOrderLine_StockableProduct(stockableProduct);
    }

    public List<DeliveryLine> getAllDeliveryLinesForPurchaseOrder(PurchaseOrder purchaseOrder) {
        log.info("GetAllDeliveryLinesForPurchaseOrder " + purchaseOrder.getPurchaseOrderReference() + " called");
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

    public List<DeliveryLine> getAllDeliveryLinesForDelivery(Long id) {
        log.info("GetAllDeliveryLinesForDelivery called");
        return deliveryLineRepository.findAllByDelivery_Id(id);
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
}
