package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.DeliveryDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.delivery.*;
import com.eep.stocker.services.DeliveryService;
import com.eep.stocker.services.SupplierService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DeliveryController {
    public static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    private final DeliveryService deliveryService;
    private final SupplierService supplierService;
    private final DeliveryMapper deliveryMapper;

    @GetMapping("/api/delivery/get/{id}")
    public GetDeliveryResponse getDeliveryById(@PathVariable String id) {
        log.info("get: /api/delivery/get/{} called", id);
        Optional<Delivery> delivery = deliveryService.getDeliveryByUid(id);
        if(delivery.isPresent()) {
            return deliveryMapper.deliveryToGetDeliveryResponse(delivery.get());
        } else {
            throw new DeliveryDoesNotExistException(String.format("Delivery with id of %d does not exist", id));
        }
    }

    @GetMapping("/api/delivery/get")
    public GetAllDeliveryResponse getAllDeliveries() {
        log.info("get: /api/delivery/get called");
        return deliveryMapper.getAllDeliveryReponseFromDeliveries(deliveryService.getAllDeliveries());
    }

    @GetMapping("/api/delivery/{fromDate}/{toDate}")
    public GetAllDeliveryResponse getAllDeliveriesBetween(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("get: /api/delivery/{}/{} called", fromDate, toDate);
        return deliveryMapper.getAllDeliveryReponseFromDeliveries(deliveryService.getAllDeliveriesBetween(fromDate, toDate));
    }

    @GetMapping("/api/delivery/supplier/{supplierId}")
    public GetAllDeliveryResponse getAllDeliveriesForSupplier(@PathVariable Long supplierId) {
        log.info("get: /api/delivery/supplier/{} called", supplierId);
        Optional<Supplier> supplier = supplierService.getSupplierFromId(supplierId);
        if(supplier.isPresent()) {
            return deliveryMapper.getAllDeliveryReponseFromDeliveries(deliveryService.getAllDeliveriesForSupplier(supplier.get()));
        } else {
            throw new SupplierDoesNotExistException("Supplier with id of " + supplierId + " does not exist");
        }
    }

    @PostMapping("/api/delivery/create")
    public GetDeliveryResponse createDelivery(@RequestBody @Valid CreateDeliveryRequest delivery) {
        log.info("create: /api/delivery/create called");
        var supplier = supplierService.getSupplierFromUid(delivery.getSupplierId());
        var newDel = deliveryMapper.map(delivery);
        supplier.ifPresent(newDel::setSupplier);
        var savedDelivery = deliveryService.saveDelivery(newDel);
        var response = deliveryMapper.deliveryToGetDeliveryResponse(savedDelivery);
        return response;
    }

    @PutMapping("/api/delivery/update")
    public GetDeliveryResponse updateDelivery(@RequestBody @Valid UpdateDeliveryRequest updateDeliveryRequest) {
        log.info("put: /api/delivery/update called");
        var deliveryOpt = deliveryService.getDeliveryByUid(updateDeliveryRequest.getId());
        var delivery = deliveryOpt.orElseThrow(() -> new DeliveryDoesNotExistException("Delivery Does Not Exist"));
        deliveryMapper.update(updateDeliveryRequest, delivery);
        var res = deliveryService.saveDelivery(delivery);
        return deliveryMapper.deliveryToGetDeliveryResponse(res);
    }

    @DeleteMapping("/api/delivery/delete/{id}")
    public String deleteDelivery(@PathVariable String id) {
        log.info("delete: /api/delivery/delete {} called", id);
        Optional<Delivery> delivery = deliveryService.getDeliveryByUid(id);
        if(delivery.isPresent()) {
            deliveryService.deleteDelivery(delivery.get());
            return "Delivery with ID of " + delivery.get().getUid().toString() + " deleted";
        } else {
            throw new DeliveryDoesNotExistException("Delivery with ID of " + id + " does not exist");
        }
    }
}
