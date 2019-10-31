package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.PurchaseOrderDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.PurchaseOrderLineService;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class PurchaseOrderLineController {
    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderLineController.class);

    private PurchaseOrderLineService orderLineService;
    private StockableProductService stockableProductService;
    private PurchaseOrderService purchaseOrderService;
    private SupplierService supplierService;

    public PurchaseOrderLineController(PurchaseOrderLineService purchaseOrderLineService,
                                       StockableProductService stockableProductService,
                                       PurchaseOrderService purchaseOrderService,
                                       SupplierService supplierService) {
        this.orderLineService = purchaseOrderLineService;
        this.stockableProductService = stockableProductService;
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
    }

    @GetMapping("/api/purchase-order-line/get")
    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        log.info("get: /api/purchase-order-line/get called");
        return orderLineService.getAllPurchaseOrderLines();
    }

    @GetMapping("/api/purchase-order-line/get/product/{productId}")
    public List<PurchaseOrderLine> getAllPurchaserOrderLinesForProduct(@PathVariable Long productId) {
        log.info("get: /api/purchase-order-line/get/product/" + productId + " called");
        Optional<StockableProduct> product = stockableProductService.getStockableProductByID(productId);

        if(product.isPresent()) {
            log.info("Stockable Product = " + product.get());
            return orderLineService.getAllPurchaseOrderLinesForProduct(product.get());
        } else {
            throw new StockableProductDoesNotExistException("Stockable product with ID of " + productId + " does not exist");
        }
    }

    @GetMapping("/api/purchase-order-line/get/purchase-order/{purchaseOrderId}")
    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForPurchaseOrder(@PathVariable Long purchaseOrderId) {
        log.info("get: /api/purchase-order-line/get/purchase-order/" + purchaseOrderId + " called");
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromId(purchaseOrderId);

        if(purchaseOrder.isPresent()) {
            return orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(purchaseOrder.get());
        } else {
            log.info("Purchase order with ID of " + purchaseOrderId + " does not exist");
            throw new PurchaseOrderDoesNotExistException("Purchase order with ID of " + purchaseOrderId + " does not exist");
        }
    }

    @GetMapping("/api/purchase-order-line/get/supplier/{supplierId}")
    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForSupplier(@PathVariable Long supplierId) {
        log.info("get: /api/purchase-order-line/get/supplier/" + supplierId + " called");
        Optional<Supplier> supplier = supplierService.getSupplierFromId(supplierId);

        if(supplier.isPresent()) {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier.get());
            List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<>();
            purchaseOrders.stream().forEach(purchaseOrder -> {
                purchaseOrderLines.addAll(orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(purchaseOrder));
            });
            return purchaseOrderLines;
        } else {
            log.info("Supplier with ID of " + supplierId + " does not exist");
            throw new SupplierDoesNotExistException("Supplier with ID of " + supplierId + " does not exist");
        }
    }

    @PostMapping("/api/purchase-order-line/save")
    public PurchaseOrderLine savePurchaseOrder(@RequestBody @Valid PurchaseOrderLine purchaseOrderLine) {
        log.info("post: /api/purchase-order-line/save called");
        return orderLineService.savePurchaseOrderLine(purchaseOrderLine);
    }

    @PutMapping("/api/purchase-order-line/update")
    public PurchaseOrderLine updatePurchaseOrder(@RequestBody @Valid PurchaseOrderLine purchaseOrderLine) {
        log.info("put: /api/purchase-order-line/update called");
        return orderLineService.savePurchaseOrderLine(purchaseOrderLine);
    }

    @DeleteMapping("/api/purchase-order-line/delete/{orderLineId}")
    public String deletePurchaseOrder(@PathVariable Long orderLineId) {
        log.info("delete: /api/purchase-order-line/delete/" + orderLineId + " called");
        Optional<PurchaseOrderLine> purchaseOrderLine = orderLineService.getPurchaseOrderLineById(orderLineId);
        if(purchaseOrderLine.isPresent()) {
            orderLineService.deletePurchaseOrderLine(purchaseOrderLine.get());
            return "Purchase Order Line with ID " + orderLineId + " has been deleted";
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase Order Line with ID " + orderLineId + " does not exist");
        }

    }
}
