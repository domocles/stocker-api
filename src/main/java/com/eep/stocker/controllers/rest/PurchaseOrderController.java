package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.PurchaseOrderDoesNotExistException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Status;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.purchaseorder.*;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/***
 * Rest controller for the {@link PurchaseOrder}, provides end points for creating, updating, deleting and searching
 * for PurchaseOrders.
 *
 * PurchaseOrders interact with the world via their unique id and not the database id.  UID are validated before being
 * passed to the end point to make sure that they are a valid UUID.
 *
 * Get methods - this controller provides endpoints for getting purchase orders by uid, to get all purchase orders, to
 * get purchase orders by supplier, get purchase orders between a date and get purchase orders by reference
 *
 * Post methods - this controller provides an endpoint for creating a purchase order
 *
 * Put methods - this controller provides an endpoint for updating a purchase order
 */
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/purchase-order")
@Slf4j
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final PurchaseOrderMapper mapper;

    /***
     * Get a specific purchase order by its uid
     * @param uid - unique id of the purchase order
     * @return = {@code GetPurchaseOrderResponse} of the purchase order
     */
    @GetMapping(value = "/{uid}")
    public GetPurchaseOrderResponse getPurchaseOrderById(@PathVariable @ValidUUID(message = "Purchase Order Id must be a UUID") String uid) {
        log.info("get: /api/purchase-order/{} called", uid);
        var uuid = UUID.fromString(uid);
        var po = this.purchaseOrderService.getPurchaseOrderFromUid(uuid).orElseThrow(() -> new DomainObjectDoesNotExistException("Purchase Order Does Not Exist"));
        return mapper.mapGetResponse(po);
    }

    /***
     * Get all purchase orders
     * @return {@code GetAllPurchaseOrdersResponse} containing all purchase orders
     */
    @GetMapping("/")
    public GetAllPurchaseOrdersResponse getAllPurchaseOrders() {
        log.info("get: /api/purchase-order/get called");
        var response = new GetAllPurchaseOrdersResponse();
        var purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        purchaseOrders.stream()
                .map(mapper::mapGetResponse)
                .forEach(response::addPurchaseOrder);
        return response;
    }

    /***
     * Fetches all purchase orders with a specific reference.  Purchase Order reference may or may not be unique,
     * the supplier order reference may be a duplicate but a supplier + order reference should be unique as a
     * composite key.
     * @param ref - the reference of the purchase order
     * @return a list containing all the purchase orders with the given reference, else empty list
     */
    @GetMapping("/supplier-reference/{ref}")
    public GetAllPurchaseOrdersResponse getPurchaseOrdersBySupplierReference(@PathVariable String ref) {
        log.info("get: /api/purchase-order/supplier-reference/{}", ref);

        var purchaseOrders = purchaseOrderService.getAllPurchaseOrdersBySupplierReference(ref);
        var response = new GetAllPurchaseOrdersResponse();
        if(purchaseOrders.isEmpty())
            return response;

        purchaseOrders.stream()
                .map(mapper::mapGetResponse)
                .forEach(response::addPurchaseOrder);

        return response;
    }

    /***
     * Fetches a purchase order by its reference which should be unique so will return a maximum of one purchase order.
     * If no purchase order with the reference exists then a PurchaseOrderDoesNotExistsException is thrown.
     * @param ref the purchase order reference to find
     * @return an GetPurchaseOrderResponse of the order if it is found, else a {@code PurchaseOrderDoesNotExistException}
     * is thrown which returns a Http.NOT_FOUND
     */
    @GetMapping("/supplier/reference/{ref}")
    public GetPurchaseOrderResponse getPurchaseOrderByReference(@PathVariable String ref) {
        log.info("get: /api/purchase-order/reference/{}", ref);
        var purchaseOrder = purchaseOrderService.getPurchaseOrderByReference(ref)
                .orElseThrow(() -> new PurchaseOrderDoesNotExistException(String.format("Purchase order with ref: %s, does not exist", ref)));
        return mapper.mapGetResponse(purchaseOrder);
    }

    /***
     * Fetches all the purchase orders for a specific supplier
     * @param supplierId - the id of the supplier who's purchase orders we want to return
     * @return {@code GetAllPurchaseOrdersResponse} containing all purchase orders of the supplier
     */
    @GetMapping("/supplier/{supplierId}")
    public GetAllPurchaseOrdersResponse getAllPurchaseOrdersForSupplier(@PathVariable @ValidUUID(message = "Supplier Id must be a UUID") String supplierId) {
        log.info("get: /api/purchase-order/supplier/{} called", supplierId);
        Optional<Supplier> supplier = this.supplierService.getSupplierFromUid(supplierId);
        if(supplier.isPresent()) {
            var response = new GetAllPurchaseOrdersResponse();
            var purchaseOrders = purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier.get());
            purchaseOrders.stream()
                    .map(mapper::mapGetResponse)
                    .forEach(response::addPurchaseOrder);
            return response;
        } else {
            throw new DomainObjectDoesNotExistException("Supplier with id of " + supplierId + " does not exist");
        }
    }

    /***
     * Fetches all purchase orders between specific dates
     * @param fromDate - the start date of the range
     * @param toDate - the end date of the range
     * @return {@code GetAllPurchaseOrdersResponse} containing all purchase orders between the supplied date range
     */
    @GetMapping("/{fromDate}/{toDate}")
    public GetAllPurchaseOrdersResponse getAllPurchaseOrdersBetween(@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                    @PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("get: /api/purchase-order/{}/{} called", fromDate, toDate);
        var response = new GetAllPurchaseOrdersResponse();
        var purchaseOrders = purchaseOrderService.getAllPurchaseOrdersBetween(fromDate, toDate);
        purchaseOrders.stream()
                .map(mapper::mapGetResponse)
                .forEach(response::addPurchaseOrder);
        return response;
    }

    /***
     * Creates a new purchase order
     * @param purchaseOrderRequest - information about the purchase order to create
     * @return {@code CreatePurchaseOrderResponse} containing the purchase order.
     */
    @PostMapping("/")
    public CreatePurchaseOrderResponse createPurchaseOrder(@RequestBody @Valid CreatePurchaseOrderRequest purchaseOrderRequest) {
        log.info("post: /api/purchase-order/create called");
        var supplier = supplierService.getSupplierFromUid(purchaseOrderRequest.getSupplierId())
                .orElseThrow(() -> new DomainObjectDoesNotExistException("Supplier does not exist"));
        var purchaseOrder = PurchaseOrder.builder()
                .purchaseOrderDate(LocalDate.now())
                .purchaseOrderReference(purchaseOrderRequest.getPurchaseOrderReference())
                .supplier(supplier)
                .status(Status.OPEN)
                .build();

        purchaseOrder = purchaseOrderService.savePurchaseOrder(purchaseOrder);

        return mapper.mapCreateResponse(purchaseOrder);
    }

    /***
     * Update the purchase order defined by the UID with the UpdatePurchaseOrderRequest
     * @param uuid unique id of the purchase order
     * @param request defines the values to update the purchase order with
     * @return {@code UpdatePurchaseOrderResponse} containing the updated purchase order
     */
    @PutMapping("/{uuid}")
    public UpdatePurchaseOrderResponse updatePurchaseOrder(@PathVariable @ValidUUID(message = "Purchase Order Id must be a UUID") String uuid,
                                                           @RequestBody @Valid UpdatePurchaseOrderRequest request) {
        log.info("put: /api/purchase-order/{}", uuid);
        var purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.fromString(uuid)).orElseThrow(() -> new DomainObjectDoesNotExistException("Purchase Order does not exists"));
        Supplier supplier = purchaseOrder.getSupplier();
        if(!purchaseOrder.getSupplier().getUid().equals(UUID.fromString(request.getSupplierId()))){
            supplier = supplierService.getSupplierFromUid(request.getSupplierId()).orElseThrow(() -> new DomainObjectDoesNotExistException("Supplier does not exist"));
            purchaseOrder.setSupplier(supplier);
        }
        mapper.updateFromRequest(purchaseOrder, request, supplier);
        purchaseOrder = purchaseOrderService.savePurchaseOrder(purchaseOrder);
        return mapper.mapToUpdateResponse(purchaseOrder);
    }

    /***
     * Delete a purchase order defined by its UUID
     * @param uuid unique id of the purchase order
     * @return a String detailing that the purchase order that was deleted
     */
    @DeleteMapping("/{uuid}")
    public String deletePurchaseOrder(@PathVariable @ValidUUID(message = "Purchase Order Id must be a UUID") String uuid) {
        log.info("delete: /api/purchase-order/{} called", uuid);
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.fromString(uuid));
        if(purchaseOrder.isPresent()) {
            purchaseOrderService.deletePurchaseOrder(purchaseOrder.get());
            return "Purchase Order with ID " + uuid + " deleted";
        } else {
            throw new PurchaseOrderDoesNotExistException("Purchase Order with ID of " + uuid + " does not exist");
        }
    }

}
