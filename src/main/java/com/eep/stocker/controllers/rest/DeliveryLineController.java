package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.*;
import com.eep.stocker.domain.*;
import com.eep.stocker.services.DeliveryLineService;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class DeliveryLineController {
    public static final Logger log = LoggerFactory.getLogger(DeliveryLineController.class);

    private DeliveryLineService deliveryLineService;
    private SupplierService supplierService;
    private StockableProductService stockableProductService;
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    public DeliveryLineController(DeliveryLineService deliveryLineService, SupplierService supplierService,
                                  StockableProductService stockableProductService, PurchaseOrderService purchaseOrderService) {
        this.deliveryLineService = deliveryLineService;
        this.supplierService = supplierService;
        this.stockableProductService = stockableProductService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/api/delivery-line/get/{id}")
    public DeliveryLine getDeliveryLineById(@PathVariable Long id) {
        log.info("get: /api/delivery-line/get/" + id + " called");
        Optional<DeliveryLine> deliveryLine = deliveryLineService.getDeliveryLineById(id);
        if(deliveryLine.isPresent()) {
            return  deliveryLine.get();
        } else {
            throw new DeliveryDoesNotExistException("DeliveryLine with ID of " + id + " does not exist");
        }
    }

    @GetMapping("/api/delivery-line/get/delivery/{id}")
    public List<DeliveryLine> getDeliveryLineByDelivery(@PathVariable Long id) {
        log.info("get: /api/delivery-line/get/delivery/" + id + " called");
       return deliveryLineService.getAllDeliveryLinesForDelivery(id);
    }

    @GetMapping("/api/delivery-line/get")
    public List<DeliveryLine> getAllDeliveryLines() {
        log.info("get: /api/delivery-line/get");
        return deliveryLineService.getAllDeliveryLines();
    }

    @GetMapping("/api/delivery-line/get/supplier/{id}")
    public List<DeliveryLine> getAllDeliveryLinesForSupplier(@PathVariable Long id) {
        log.info("get: /api/delivery-line/get/supplier/" + id + " called");
        Optional<Supplier> supplier = supplierService.getSupplierFromId(id);
        if(supplier.isPresent()) {
            return deliveryLineService.getAllDeliveryLinesForSupplier(supplier.get());
        } else {
            throw new SupplierDoesNotExistException("Supplier with ID of " + id + " does not exist");
        }
    }

    @GetMapping("/api/delivery-line/get/stockable-product/{id}")
    public List<DeliveryLine> getAllDeliveryLinesForStockableProduct(@PathVariable Long id) {
        log.info("get: /api/delivery-line/get/supplier/" + id + " called");
        Optional<StockableProduct> product = stockableProductService.getStockableProductByID(id);
        if(product.isPresent()) {
            return deliveryLineService.getAllDeliveryLinesForStockableProduct(product.get());
        } else {
            throw new StockableProductDoesNotExistException("Stockable Product with ID of " + id + " does not exist");
        }
    }

    @GetMapping("/api/delivery-line/get/purchase-order/{id}")
    public List<DeliveryLine> getAllDeliveryLinesForPurchaseOrder(@PathVariable Long id) {
        log.info("get: /api/delivery-line/get/purchase-order/" + id + " called");
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromId(id);
        if(purchaseOrder.isPresent()) {
            return deliveryLineService.getAllDeliveryLinesForPurchaseOrder(purchaseOrder.get());
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase Order with ID of " + id + " does not exist");
        }
    }

    @DeleteMapping("/api/delivery-line/delete/{id}")
    public String deleteDeliveryLine(@PathVariable Long id) {
        log.info("delete: /api/delivery-line/delete/" + id + " called");
        Optional<DeliveryLine> deliveryLine = deliveryLineService.getDeliveryLineById(id);
        if(deliveryLine.isPresent()) {
            deliveryLineService.deleteDeliveryLine(deliveryLine.get());
            return "Delivery Line with ID of " + id + " deleted";
        } else {
            throw new DeliveryLineDoesNotExistException("Delivery Line with ID of " + id + " does not exist");
        }
    }

    @PostMapping("/api/delivery-line/create")
    public DeliveryLine createDeliveryLine(@RequestBody @Valid DeliveryLine deliveryLine) {
        log.info("post: /api/delivery-line/create called");
        deliveryLine = deliveryLineService.save(deliveryLine);
        return deliveryLine;
    }

    @PutMapping("/api/delivery-line/update")
    public DeliveryLine updateDeliveryLine(@RequestBody @Valid DeliveryLine deliveryLine) {
        log.info("post: /api/delivery-line/create called");
        deliveryLine = deliveryLineService.save(deliveryLine);
        return deliveryLine;
    }
}
