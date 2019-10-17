package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IPurchaseOrderLineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderLineService {
    private IPurchaseOrderLineRepository purchaseOrderLineRepository;

    public PurchaseOrderLineService(IPurchaseOrderLineRepository purchaseOrderLineRepository) {
        this.purchaseOrderLineRepository = purchaseOrderLineRepository;
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineRepository.findAll();
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForProduct(StockableProduct stockableProduct) {
        return purchaseOrderLineRepository.findAllByStockableProduct(stockableProduct);
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderLineRepository.findAllByPurchaseOrder(purchaseOrder);
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
}
