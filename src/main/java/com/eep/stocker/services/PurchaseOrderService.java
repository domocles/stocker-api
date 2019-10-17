package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.IPurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderService.class);

    private IPurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(IPurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public List<PurchaseOrder> getAllPurchaseOrdersForSupplier(Supplier supplier) {
        return purchaseOrderRepository.findAllBySupplier(supplier);
    }

    public List<PurchaseOrder> getAllPurchaseOrdersBetween(Date start, Date end) {
        return purchaseOrderRepository.findAllByPurchaseOrderDateBetween(start, end);
    }

    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public Optional<PurchaseOrder> getPurchaseOrderFromId(Long purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId);
    }

    public void deletePurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderRepository.delete(purchaseOrder);
    }
}
