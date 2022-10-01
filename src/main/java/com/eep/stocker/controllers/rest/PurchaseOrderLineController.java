package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.PurchaseOrderDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.PurchaseOrderLineDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.*;
import com.eep.stocker.dto.purchaseorderline.*;
import com.eep.stocker.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/09/2022
 *
 * Purchase Order Line Controller
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/api/purchase-order-line")
public class PurchaseOrderLineController {
    private final PurchaseOrderLineService orderLineService;
    private final StockableProductService stockableProductService;
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final DeliveryLineService deliveryLineService;

    private final PurchaseOrderLineMapper mapper;

    /***
     * Returns a purchase order line found by its unique id
     * @param uid unique id of the purchase order line
     * @return a {@code GetPurchaseOrderLineResponse} or {@code HttpStatus.NOT_FOUND}
     */
    @GetMapping("/{uid}")
    public GetPurchaseOrderLineResponse getPurchaseOrderLineResponseByUid(@PathVariable @ValidUUID(message = "Purchase Order Line ID needs to e a UUID") String uid) {
        log.info("get: /api/purchase-order-line/{} called", uid);
        var orderLine = orderLineService.getPurchaseOrderLineByUid(uid)
                .orElseThrow(() -> new PurchaseOrderLineDoesNotExistException(String.format("Purchase Order Line with uid of %s, does not exist", uid)));
        return mapper.mapToGetResponse(orderLine);
    }

    /***
     * Returns all purchase order lines
     * @return a {@code GetAllPurchaseOrderLinesResponse} containing all purchase order lines
     */
    @GetMapping("/")
    public GetAllPurchaseOrderLinesResponse getAllPurchaseOrderLines() {
        log.info("get: /api/purchase-order-line/get called");
        var response = new GetAllPurchaseOrderLinesResponse();
        var orderLines = orderLineService.getAllPurchaseOrderLines();
        orderLines.stream()
                .map(mapper::mapToGetLowDetailResponse)
                .forEach(response::addPurchaseOrderLine);
        return response;
    }

    /***
     * Fetches all purchase orders for a specific stockable product
     * @param productId - the unique id of the product
     * @return a {@code GetPurchaseOrderLinesByProductResponse} containing all purchase order lines
     */
    @GetMapping("/product/{productId}/")
    public GetPurchaseOrderLinesByProductResponse getAllPurchaserOrderLinesForProduct(@PathVariable @ValidUUID(message = "Product ID needs to e a UUID") String productId) {
        log.info("get: /purchase-order-line/product/{} called", productId);
        var product = stockableProductService.getStockableProductByUid(productId).orElseThrow(
                () -> new StockableProductDoesNotExistException(String.format("Stockable product with id of %s, does not exist", productId)));
        log.info("Stockable Product = {}", product.getName());
        var orderLines = orderLineService.getAllPurchaseOrderLinesForProduct(product);
        var response = new GetPurchaseOrderLinesByProductResponse();
        orderLines.stream()
                .map(mapper::mapToGetLowDetailResponse)
                .forEach(response::addPurchaseOrderLine);
        return response;
    }

    /***
     * Fetches all purchase order lines for a specific purchase order
     * @param purchaseOrderId the unique id of the purchase order to get the purchase order lines for
     * @return a {@code GetPurchaseOrderLinesByPurchaseOrderResponse} with all lines for a purchase order
     */
    @GetMapping("/purchase-order/{purchaseOrderId}/")
    public GetPurchaseOrderLinesByPurchaseOrderResponse getAllPurchaseOrderLinesForPurchaseOrder(@PathVariable @ValidUUID(message = "Purchase Order ID needs to e a UUID") String purchaseOrderId) {
        log.info("get: /api/purchase-order-line/get/purchase-order/{} called", purchaseOrderId);
        var purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.fromString(purchaseOrderId)).orElseThrow(
                () -> new PurchaseOrderDoesNotExistException(String.format("Purchase order with id of %s, does not exist", purchaseOrderId)));
        var response = new GetPurchaseOrderLinesByPurchaseOrderResponse();
        var orderLines = orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(purchaseOrder);
        orderLines.stream()
                .map(mapper::mapToGetLowDetailResponse)
                .forEach(response::addPurchaseOrderLine);
        return response;
    }

    /***
     * Gets the remaining balance of a purchase order line
     * @param purchaseOrderLineId - the purchase order line to get the balance of
     * @return - the balance of remaining parts to be delivered
     */
    @GetMapping("/balance/{purchaseOrderLineId}")
    public Double getBalanceOfPurchaseOrderLine(@PathVariable @ValidUUID(message = "Purchase Order Line ID needs to e a UUID") String purchaseOrderLineId) {
        log.info("get: /api/purchase-order-line/balance/{} called", purchaseOrderLineId);
        Optional<PurchaseOrderLine> poLineOptional = orderLineService.getPurchaseOrderLineByUid(purchaseOrderLineId);
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

    /***
     * Get all purchase order lines by supplier
     * @param supplierId - uuid of the supplier
     * @return a {@code GetPurchaseOrderLinesBySupplierResponse} containing the order lines that belong to that supplier
     */
    @GetMapping("/supplier/{supplierId}/")
    public GetPurchaseOrderLinesBySupplierResponse getAllPurchaseOrderLinesForSupplier(@PathVariable @ValidUUID(message = "Supplier ID needs to e a UUID") String supplierId) {
        log.info("get: /api/purchase-order-line/get/supplier/{} called", supplierId);
        Optional<Supplier> supplier = supplierService.getSupplierFromUid(supplierId);

        if(supplier.isPresent()) {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier.get());
            var response = new GetPurchaseOrderLinesBySupplierResponse();
            purchaseOrders.stream()
                    .flatMap(order -> orderLineService.getAllPurchaseOrderLinesForPurchaseOrder(order).stream())
                    .forEach(orderLine -> {
                    response.allPurchaseOrderLines.add(mapper.mapToGetLowDetailResponse(orderLine));
            });
            return response;
        } else {
            log.info("Supplier with ID of {} does not exist", supplierId);
            throw new SupplierDoesNotExistException("Supplier with ID of " + supplierId + " does not exist");
        }
    }

    /***
     * Create a new Purchase Order Line
     * @param request - details of the purchase order line to request
     * @return {@code CreatePurchaseOrderLineResponse} that returns the persisted Purchase Order Line
     */
    @PostMapping("/")
    public CreatePurchaseOrderLineResponse savePurchaseOrder(@RequestBody @Valid CreatePurchaseOrderLineRequest request) {
        log.info("post: /api/purchase-order-line/save called");
        var purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.fromString(request.getPurchaseOrderId()))
                .orElseThrow(() -> new PurchaseOrderDoesNotExistException("Purchase Order Does Not Exist"));
        var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
        var purchaseOrderLine = mapper.mapCreateRequestToPurchaseOrderLine(request);
        purchaseOrderLine.setPurchaseOrder(purchaseOrder);
        purchaseOrderLine.setStockableProduct(stockableProduct);
        purchaseOrderLine.setStatus(Status.OPEN);
        return mapper.mapToCreateResponse(orderLineService.savePurchaseOrderLine(purchaseOrderLine));
    }

    /***
     * Update a purchase order line
     * @param uid - the purchase order line to update
     * @param request - contains the update details
     * @return {@code UpdatePurchaseOrderLineResponse} that returns the updated purchase order line
     */
    @PutMapping("/{uid}")
    public UpdatePurchaseOrderLineResponse updatePurchaseOrder(@PathVariable @ValidUUID(message = "Purchase Order Line ID needs to e a UUID") String uid,
                                                               @RequestBody @Valid UpdatePurchaseOrderLineRequest request) {
        log.info("put: /api/purchase-order-line/{} called", uid);
        var purchaseOrderLine = orderLineService.getPurchaseOrderLineByUid(uid)
                .orElseThrow(() -> new PurchaseOrderLineDoesNotExistException("Purchase Order Line Does Not Exist"));
        mapper.updateFromRequest(purchaseOrderLine, request);
        if(!request.getPurchaseOrderId().equals(purchaseOrderLine.getPurchaseOrder().getUid().toString())) {
            var purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.fromString(request.getPurchaseOrderId()))
                    .orElseThrow(() -> new PurchaseOrderDoesNotExistException("Purchase Order Does Not Exist"));
            purchaseOrderLine.setPurchaseOrder(purchaseOrder);
        }
        if(!request.getStockableProductId().equals(purchaseOrderLine.getStockableProduct().getUid().toString())) {
            var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                    .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
            purchaseOrderLine.setStockableProduct(stockableProduct);
        }

        return mapper.mapToUpdateResponse(orderLineService.savePurchaseOrderLine(purchaseOrderLine));
    }

    /***
     * Delete a purchase order line by its id
     * @param orderLineId - the uid of the purchase order line
     * @return a message confirming the deletion of the purchase order line
     */
    @DeleteMapping("/{orderLineId}")
    public String deletePurchaseOrder(@PathVariable @ValidUUID(message = "Purchase Order Line ID needs to e a UUID") String orderLineId) {
        log.info("delete: /api/purchase-order-line/{} called", orderLineId);

        var purchaseOrderLine = orderLineService.getPurchaseOrderLineByUid(orderLineId)
                .orElseThrow(() -> new PurchaseOrderDoesNotExistException("Purchase Order Line with ID " + orderLineId + " does not exist"));

        orderLineService.deletePurchaseOrderLine(purchaseOrderLine);

        return String.format("Purchase Order Line with ID %s has been deleted", orderLineId);
    }
}
