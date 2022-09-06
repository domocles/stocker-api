package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.IDeliveryLineRepository;
import com.eep.stocker.repository.IPurchaseOrderLineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderLineService {
    private IPurchaseOrderLineRepository purchaseOrderLineRepository;
    private IDeliveryLineRepository deliveryLineRepository;

    public PurchaseOrderLineService(IPurchaseOrderLineRepository purchaseOrderLineRepository,
                                    IDeliveryLineRepository deliveryLineRepository) {
        this.purchaseOrderLineRepository = purchaseOrderLineRepository;
        this.deliveryLineRepository = deliveryLineRepository;
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineRepository.findAll();
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForProduct(StockableProduct stockableProduct) {
        return purchaseOrderLineRepository.findAllByStockableProduct(stockableProduct);
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForPurchaseOrder(PurchaseOrder purchaseOrder) {
        List<PurchaseOrderLine> orderLines = purchaseOrderLineRepository.findAllByPurchaseOrder(purchaseOrder);
        for(PurchaseOrderLine poLine : orderLines) {
            Optional<Double> delivered = deliveryLineRepository.getSumOfDeliveriesForPurchaseOrderLine(poLine);
            if(delivered.isPresent()) {
                double balance = poLine.getQty() - delivered.get();
                poLine.setBalance(balance);
            }
        }
        return orderLines;
    }

    public PurchaseOrderLine savePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        return purchaseOrderLineRepository.save(purchaseOrderLine);
    }

    public Optional<PurchaseOrderLine> getPurchaseOrderLineById(Long orderLineId) {
        return purchaseOrderLineRepository.findById(orderLineId);
    }

    public void deletePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        purchaseOrderLineRepository.delete(purchaseOrderLine);
    }

    public Optional<PurchaseOrderLine> getPurchaseOrderLineByUid(String uid) {
        return purchaseOrderLineRepository.findByUid(uid);
    }
}
