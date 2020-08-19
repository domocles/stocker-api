package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.DeliveryDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.DeliveryService;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class DeliveryController {
    public static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    private DeliveryService deliveryService;
    private SupplierService supplierService;

    public DeliveryController(DeliveryService deliveryService,
                              SupplierService supplierService) {
        this.deliveryService = deliveryService;
        this.supplierService = supplierService;
    }

    @GetMapping("/api/delivery/get/{id}")
    public Delivery getDeliveryById(@PathVariable Long id) {
        log.info("get: /api/delivery/get/{} called", id);
        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);
        if(delivery.isPresent()) {
            return delivery.get();
        } else {
            throw new DeliveryDoesNotExistException(String.format("Delivery with id of %d does not exist", id));
        }
    }

    @GetMapping("/api/delivery/get")
    public List<Delivery> getAllDeliveries() {
        log.info("get: /api/delivery/get called");
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("/api/delivery/{fromDate}/{toDate}")
    public List<Delivery> getAllDeliveriesBetween(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        log.info("get: /api/delivery/{}/{} called", fromDate, toDate);
        return deliveryService.getAllDeliveriesBetween(fromDate, toDate);
    }

    @GetMapping("/api/delivery/supplier/{supplierId}")
    public List<Delivery> getAllDeliveriesForSupplier(@PathVariable Long supplierId) {
        log.info(String.format("get: /api/delivery/supplier/%d called", supplierId));
        Optional<Supplier> supplier = supplierService.getSupplierFromId(supplierId);
        if(supplier.isPresent()) {
            return deliveryService.getAllDeliveriesForSupplier(supplier.get());
        } else {
            throw new SupplierDoesNotExistException("Supplier with id of " + supplierId + " does not exist");
        }
    }

    @PostMapping("/api/delivery/create")
    public Delivery createDelivery(@RequestBody @Valid Delivery delivery) {
        log.info("put: /api/delivery/create called");
        return deliveryService.saveDelivery(delivery);
    }

    @PutMapping("/api/delivery/update")
    public Delivery updateDelivery(@RequestBody @Valid Delivery delivery) {
        log.info("put: /api/delivery/update called");
        return deliveryService.saveDelivery(delivery);
    }

    @DeleteMapping("/api/delivery/delete/{id}")
    public String deleteDelivery(@PathVariable Long id) {
        log.info("delete: /api/delivery/delete " + id + " called");
        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);
        if(delivery.isPresent()) {
            deliveryService.deleteDelivery(delivery.get());
            return "Delivery with ID of " + id + " deleted";
        } else {
            throw new DeliveryDoesNotExistException("Delivery with ID of " + id + " does not exist");
        }
    }
}
