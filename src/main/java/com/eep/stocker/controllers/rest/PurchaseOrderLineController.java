package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.PurchaseOrderDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.PurchaseOrderLineService;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.StockableProductService;
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

    public PurchaseOrderLineController(PurchaseOrderLineService purchaseOrderLineService,
                                       StockableProductService stockableProductService,
                                       PurchaseOrderService purchaseOrderService) {
        this.orderLineService = purchaseOrderLineService;
        this.stockableProductService = stockableProductService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/api/purchase-order-line/get")
    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        log.info("/api/purchase-order-line/get called");
        return orderLineService.getAllPurchaseOrderLines();
    }

    @GetMapping("/api/purchase-order-line/get/product/{productId}")
    public List<PurchaseOrderLine> getAllPurchaserOrderLinesForProduct(@PathVariable Long productId) {
        log.info("/api/purchase-order-line/get/product/" + productId + " called");
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
        log.info("/api/purchase-order-line/het/purchase-order/" + purchaseOrderId + " called");
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromId(purchaseOrderId);

        if(purchaseOrder.isPresent()) {
            return orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(purchaseOrder.get());
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase order with ID of " + purchaseOrderId + " does not exist");
        }
    }

    @PostMapping("/api/purchase-order-line/save")
    public PurchaseOrderLine savePurchaseOrder(@RequestBody @Valid PurchaseOrderLine purchaseOrderLine) {
        return orderLineService.savePurchaseOrderLine(purchaseOrderLine);
    }

    @PutMapping("api/purchase-order-line/update")
    public PurchaseOrderLine updatePurchaseOrder(@RequestBody @Valid PurchaseOrderLine purchaseOrderLine) {
        return orderLineService.savePurchaseOrderLine(purchaseOrderLine);
    }

    @DeleteMapping("api/purchase-order-line/delete/{orderLineId}")
    public String deletePurchaseOrder(@PathVariable Long orderLineId) {
        Optional<PurchaseOrderLine> purchaseOrderLine = orderLineService.getPurchaseOrderLineById(orderLineId);
        if(purchaseOrderLine.isPresent()) {
            orderLineService.deletePurchaseOrderLine(purchaseOrderLine.get());
            return "Purchase Order Line with ID " + orderLineId + " has been deleted";
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase Order Line with ID " + orderLineId + " does not exist");
        }

    }
}
