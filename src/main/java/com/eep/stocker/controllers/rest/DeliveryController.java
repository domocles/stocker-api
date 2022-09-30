package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.DeliveryDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.dto.delivery.*;
import com.eep.stocker.services.DeliveryService;
import com.eep.stocker.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

/***
 * @author Sam Burns
 * @version 1.0
 * 12/09/2022
 *
 * Rest controller for deliveries
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final SupplierService supplierService;
    private final DeliveryMapper deliveryMapper;

    /***
     * Gets a delivery by its unique id
     * @param uid - the unique id of the delivery to fetch
     * @return a {@code GetDeliveryResponse} containing the delivery
     */
    @GetMapping("/get/{uid}")
    public GetDeliveryResponse getDeliveryById(@PathVariable @ValidUUID(message = "Delivery Id must be a UUID") String uid) {
        log.info("get: /api/delivery/get/{} called", uid);
        var delivery = deliveryService.getDeliveryByUid(uid)
                .orElseThrow(() -> new DeliveryDoesNotExistException(String.format("Delivery with id of %s does not exist", uid)));
        return deliveryMapper.deliveryToGetDeliveryResponse(delivery);
    }

    /***
     * Get all deliveries
     * @return a {@code GetAllDeliveryResponse} containing all of the deliveries
     */
    @GetMapping("/")
    public GetAllDeliveryResponse getAllDeliveries() {
        log.info("get: /api/delivery/get called");
        return deliveryMapper.getAllDeliveryReponseFromDeliveries(deliveryService.getAllDeliveries());
    }

    /***
     * Get all deliveries between the supplied dates
     * @param fromDate - the beginning date of the range to fetch
     * @param toDate - the end date of the range to fetch
     * @return a {@code GetAllDeliveryResponse} containing all of the deliveries in the range
     */
    @GetMapping("/{fromDate}/{toDate}")
    public GetAllDeliveryResponse getAllDeliveriesBetween(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("get: /api/delivery/{}/{} called", fromDate, toDate);
        return deliveryMapper.getAllDeliveryReponseFromDeliveries(deliveryService.getAllDeliveriesBetween(fromDate, toDate));
    }

    /***
     * Get all of the deliveries from a supplier
     * @param supplierId the unique of the supplier
     * @return a {@code GetAllDeliveryResponse} containing all of the deliveries from the supplier
     */
    @GetMapping("/supplier/{supplierId}")
    public GetAllDeliveryResponse getAllDeliveriesForSupplier(@PathVariable @ValidUUID(message = "Supplier Id must be a UUID") String supplierId) {
        log.info("get: /api/delivery/supplier/{} called", supplierId);
        var supplier = supplierService.getSupplierFromUid(supplierId)
                .orElseThrow(() -> new SupplierDoesNotExistException("Supplier with id of " + supplierId + " does not exist"));

        return deliveryMapper.getAllDeliveryReponseFromDeliveries(deliveryService.getAllDeliveriesForSupplier(supplier));
    }

    /***
     * Create a new delivery
     * @param delivery - the delivery to create
     * @return a {@code GetDeliveryResponse} containing the new delivery
     */
    @PostMapping("/")
    public GetDeliveryResponse createDelivery(@RequestBody @Valid CreateDeliveryRequest delivery) {
        log.info("create: /api/delivery called");
        var supplier = supplierService.getSupplierFromUid(delivery.getSupplierId());
        var newDel = deliveryMapper.map(delivery);
        supplier.ifPresent(newDel::setSupplier);
        var savedDelivery = deliveryService.saveDelivery(newDel);
        return deliveryMapper.deliveryToGetDeliveryResponse(savedDelivery);
    }

    /***
     * Update an existing delivery
     * @param uid - the unique identifier of the delivery to update
     * @param updateDeliveryRequest - the updated details of the delivery
     * @return a {@code GetDeliveryResponse} containing the new delivery
     */
    @PutMapping("/{uid}")
    public GetDeliveryResponse updateDelivery(@PathVariable @ValidUUID(message = "Delivery Id must be a UUID") String uid, @RequestBody @Valid UpdateDeliveryRequest updateDeliveryRequest) {
        log.info("put: /api/delivery called");
        var deliveryOpt = deliveryService.getDeliveryByUid(uid);
        var delivery = deliveryOpt.orElseThrow(() -> new DeliveryDoesNotExistException("Delivery Does Not Exist"));
        deliveryMapper.update(updateDeliveryRequest, delivery);
        var res = deliveryService.saveDelivery(delivery);
        return deliveryMapper.deliveryToGetDeliveryResponse(res);
    }

    /***
     * Delete a delivery by its unique identifier
     * @param uid - the id of the delivery to delete
     * @return - a confirmation message
     */
    @DeleteMapping("/delete/{uid}")
    public String deleteDelivery(@PathVariable @ValidUUID(message = "Delivery Id must be a UUID") String uid) {
        log.info("delete: /api/delivery/delete {} called", uid);
        var delivery = deliveryService.getDeliveryByUid(uid).orElseThrow(
                () -> new DeliveryDoesNotExistException("Delivery with ID of " + uid + " does not exist")
        );
        deliveryService.deleteDelivery(delivery);
        return "Delivery with ID of " + delivery.getUid().toString() + " deleted";
    }
}
