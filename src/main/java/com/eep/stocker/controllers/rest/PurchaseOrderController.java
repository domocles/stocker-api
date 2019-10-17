package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.PurchaseOrderDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PurchaseOrderController {
    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderController.class);

    private PurchaseOrderService purchaseOrderService;
    private SupplierService supplierService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, SupplierService supplierService) {
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
    }

    @GetMapping("/api/purchase-order/get")
    public List<PurchaseOrder> getAllPurchaseOrders() {
        log.info("Get all purchase orders called");
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/api/purchase-order/supplier/get/{supplierId}")
    public List<PurchaseOrder> getAllPurchaseOrdersForSupplier(@PathVariable Long supplierId) {
        log.info("Get all purchase orders for supplier: " + supplierId);
        Optional<Supplier> supplier = this.supplierService.getSupplierFromId(supplierId);
        if(supplier.isPresent()) {
            return purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier.get());
        } else {
            throw new SupplierDoesNotExistException("Supplier with id of " + supplierId + " does not exist");
        }
    }

    @GetMapping("/api/purchase-order/{fromDate}/{toDate}")
    public List<PurchaseOrder> getAllPurchaseOrdersBetween(@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate, @PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        return purchaseOrderService.getAllPurchaseOrdersBetween(fromDate, toDate);
    }

    @PostMapping("/api/purchase-order/create")
    public PurchaseOrder createPurchaseOrder(@RequestBody @Valid  PurchaseOrder purchaseOrder) {
        return purchaseOrderService.savePurchaseOrder(purchaseOrder);
    }

    @PutMapping("/api/purchase-order/update")
    public PurchaseOrder updatePurchaseOrder(@RequestBody @Valid PurchaseOrder purchaseOrder) {
        return purchaseOrderService.savePurchaseOrder(purchaseOrder);
    }

    @DeleteMapping("/api/purchase-order/delete/{purchaseOrderId}")
    public String deletePurchaseOrder(@PathVariable Long purchaseOrderId) {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromId(purchaseOrderId);
        if(purchaseOrder.isPresent()) {
            purchaseOrderService.deletePurchaseOrder(purchaseOrder.get());
            return "Purchase Order with ID " + purchaseOrderId + " deleted";
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase Order with ID of " + purchaseOrderId + " does not exist");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void supplierNotFoundHandler(SupplierDoesNotExistException ex) {}

}
