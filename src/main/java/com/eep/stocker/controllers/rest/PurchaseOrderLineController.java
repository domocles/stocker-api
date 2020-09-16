package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.PurchaseOrderDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.PurchaseOrderLineDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PurchaseOrderLineController {
    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderLineController.class);

    private PurchaseOrderLineService orderLineService;
    private StockableProductService stockableProductService;
    private PurchaseOrderService purchaseOrderService;
    private SupplierService supplierService;
    private DeliveryLineService deliveryLineService;

    public PurchaseOrderLineController(PurchaseOrderLineService purchaseOrderLineService,
                                       StockableProductService stockableProductService,
                                       PurchaseOrderService purchaseOrderService,
                                       SupplierService supplierService,
                                       DeliveryLineService deliveryLineService) {
        this.orderLineService = purchaseOrderLineService;
        this.stockableProductService = stockableProductService;
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
        this.deliveryLineService = deliveryLineService;
    }

    @GetMapping("/api/purchase-order-line/get")
    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        log.info("get: /api/purchase-order-line/get called");
        return orderLineService.getAllPurchaseOrderLines();
    }

    @GetMapping("/api/purchase-order-line/get/product/{productId}")
    public List<PurchaseOrderLine> getAllPurchaserOrderLinesForProduct(@PathVariable Long productId) {
        log.info("get: /api/purchase-order-line/get/product/{} called", productId);
        Optional<StockableProduct> product = stockableProductService.getStockableProductByID(productId);

        if(product.isPresent()) {
            log.info("Stockable Product = {}", product.get());
            return orderLineService.getAllPurchaseOrderLinesForProduct(product.get());
        } else {
            throw new StockableProductDoesNotExistException(String.format("Stockable product with ID of %s does not exist", productId));
        }
    }

    @GetMapping("/api/purchase-order-line/get/purchase-order/{purchaseOrderId}")
    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForPurchaseOrder(@PathVariable Long purchaseOrderId) {
        log.info("get: /api/purchase-order-line/get/purchase-order/{} called", purchaseOrderId);
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromId(purchaseOrderId);

        if(purchaseOrder.isPresent()) {
            return orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(purchaseOrder.get());
        } else {
            log.info("Purchase order with ID of {} does not exist", purchaseOrderId);
            throw new PurchaseOrderDoesNotExistException("Purchase order with ID of " + purchaseOrderId + " does not exist");
        }
    }

    @GetMapping("/api/purchase-order-line/get/balance/{purchaseOrderLineId}")
    public Double getBalanceOfPurchaseOrderLine(@PathVariable Long purchaseOrderLineId) {
        log.info("get: /api/purchase-order-line/get/balance/{} called", purchaseOrderLineId);
        Optional<PurchaseOrderLine> poLineOptional = orderLineService.getPurchaseOrderLineById(purchaseOrderLineId);
        if(poLineOptional.isPresent()) {
            PurchaseOrderLine poLine = poLineOptional.get();
            Optional<Double> deliveredOptional = deliveryLineService.getSumDeliveredForOrderLine(poLine);
            Double delivered;
            if(deliveredOptional.isPresent()) {
                delivered = deliveredOptional.get();
            } else {
                delivered = 0.0;
            }

            Double balance = poLine.getQty() - delivered;
            return balance;

        } else {
            log.info("Purchase order line with ID of {} does not exist", purchaseOrderLineId);
            throw  new PurchaseOrderLineDoesNotExistException("Purchase order line with ID of " + purchaseOrderLineId + " does not exist");
        }
    }

    @GetMapping("/api/purchase-order-line/get/supplier/{supplierId}")
    public List<PurchaseOrderLine> getAllPurchaseOrderLinesForSupplier(@PathVariable Long supplierId) {
        log.info("get: /api/purchase-order-line/get/supplier/{} called", supplierId);
        Optional<Supplier> supplier = supplierService.getSupplierFromId(supplierId);

        if(supplier.isPresent()) {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier.get());
            List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<>();
            purchaseOrders.stream().forEach(purchaseOrder -> {
                purchaseOrderLines.addAll(orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(purchaseOrder));
            });
            return purchaseOrderLines;
        } else {
            log.info("Supplier with ID of {} does not exist", supplierId);
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
        log.info("delete: /api/purchase-order-line/delete/{} called", orderLineId);
        Optional<PurchaseOrderLine> purchaseOrderLine = orderLineService.getPurchaseOrderLineById(orderLineId);
        if(purchaseOrderLine.isPresent()) {
            orderLineService.deletePurchaseOrderLine(purchaseOrderLine.get());
            return String.format("Purchase Order Line with ID %s has been deleted", orderLineId);
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase Order Line with ID " + orderLineId + " does not exist");
        }

    }
}
