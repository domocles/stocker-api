package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.*;
import com.eep.stocker.dto.deliveryline.*;
import com.eep.stocker.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/***
 * @author Sam Burns
 * @version 1.0
 * 12/09/2022
 *
 * Rest controller for delivery lines
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/delivery-line")
@Validated
public class DeliveryLineController {
    private final DeliveryLineService deliveryLineService;
    private final DeliveryService deliveryService;
    private final SupplierService supplierService;
    private final StockableProductService stockableProductService;
    private final StockTransactionService stockTransactionService;
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderLineService purchaseOrderLineService;
    private final DeliveryLineMapper mapper;

    /***
     * Get a delivery line by its unique identifier
     * @param uid - the unique identifier of the delivery line to get
     * @return a {@code GetDeliveryLineResponse} containing the delivery line
     */
    @GetMapping("/{uid}")
    public GetDeliveryLineResponse getDeliveryLineById(@PathVariable @ValidUUID(message = "Delivery Line Id must be a UUID") String uid) {
        log.info("get: /api/delivery-line/{} called", uid);
        var deliveryLine = deliveryLineService.getDeliveryLineByUid(uid)
                .orElseThrow(() -> new DeliveryLineDoesNotExistException(String.format("DeliveryLine with ID of %s does not exist", uid)));
        return mapper.mapToGetResponse(deliveryLine);
    }

    /***
     * Get all delivery lines for a delivery
     * @param uid - the unique identifier of the delivery
     * @return a {@code GetAllDeliveryResponse} containing the delivery lines
     */
    @GetMapping("/delivery/{uid}/")
    public GetAllByDeliveryResponse getDeliveryLineByDelivery(@PathVariable @ValidUUID(message = "Delivery Id must be a UUID") String uid) {
        log.info("get: /api/delivery-line/get/delivery/{} called", uid);
        var response = new GetAllByDeliveryResponse();
        deliveryLineService.getAllDeliveryLinesForDelivery(UUID.fromString(uid)).stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addDeliveryLine);
        return response;
    }

    /***
     * Get all delivery lines
     * @return a {@code GetAllDeliveryLinesResponse} containing all the delivery lines
     */
    @GetMapping("/")
    public GetAllDeliveryLinesResponse getAllDeliveryLines() {
        log.info("get: /api/delivery-line/");
        var response = new GetAllDeliveryLinesResponse();
        deliveryLineService.getAllDeliveryLines()
                .stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addDeliveryLine);
        return response;
    }

    /***
     * Get all delivery lines for a supplier
     * @param uid - the unique identifier of the supplier
     * @return a {@code GetDeliveryLinesBySupplierResponse} containing the delivery lines
     */
    @GetMapping("/supplier/{uid}/")
    public GetDeliveryLinesBySupplierResponse getAllDeliveryLinesForSupplier(@PathVariable @ValidUUID(message = "Supplier Id must be a UUID") String uid) {
        log.info("get: /api/delivery-line/supplier/{}/ called", uid);
        var supplier = supplierService.getSupplierFromUid(uid)
                .orElseThrow(() -> new SupplierDoesNotExistException(String.format("Supplier with ID of %s does not exist", uid)));
        var response = new GetDeliveryLinesBySupplierResponse();
        deliveryLineService.getAllDeliveryLinesForSupplier(supplier)
                .stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addDeliveryLine);
        return response;

    }

    /***
     * Get all delivery lines for a stockable product
     * @param uid - the unique identifier of the stockable product
     * @return a {@code GetDeliveryLinesByProductResponse} containing the delivery lines for the product
     */
    @GetMapping("/stockable-product/{uid}/")
    public GetDeliveryLinesByProductResponse getAllDeliveryLinesForStockableProduct(@PathVariable @ValidUUID(message = "Stockable Product Id must be a UUID") String uid) {
        log.info("get: /api/delivery-line/supplier/{}/ called", uid );
        var product = stockableProductService.getStockableProductByUid(uid)
                .orElseThrow(() -> new StockableProductDoesNotExistException(String.format("Stockable Product with ID of %s does not exist", uid)));
        var response = new GetDeliveryLinesByProductResponse();
        deliveryLineService.getAllDeliveryLinesForStockableProduct(product).stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addDeliveryLine);
        return response;

    }

    /***
     * Get all delivery lines for a purchase order
     * @param uid - the unique identifier of the purchase order
     * @return a {@code GetDeliveryLinesByPurchaseOrderResponse} containing the delivery lines for a purchase order
     */
    @GetMapping("/purchase-order/{uid}/")
    public GetDeliveryLinesByPurchaseOrderResponse getAllDeliveryLinesForPurchaseOrder(@PathVariable @ValidUUID(message = "Purchase Order Id must be a UUID") String uid) {
        log.info("get: /api/delivery-line/get/purchase-order/{}/ called", uid);
        var purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.fromString(uid))
                .orElseThrow(() -> new PurchaseOrderDoesNotExistException("Purchase Order with ID of " + uid + " does not exist"));

        var response = new GetDeliveryLinesByPurchaseOrderResponse();
        deliveryLineService.getAllDeliveryLinesForPurchaseOrder(purchaseOrder).stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addDeliveryLine);

        return response;

    }

    /***
     * Delete a delivery line based on its unique id
     * @param uid - unique identifier of the delivery line
     * @return a {@code String} confirming the operation was completed
     */
    @DeleteMapping("/{uid}")
    public String deleteDeliveryLine(@PathVariable @ValidUUID(message = "Delivery Line Id must be a UUID") String uid) {
        log.info("delete: /api/delivery-line/delete/{}", uid);
        var deliveryLine = deliveryLineService.getDeliveryLineByUid(uid)
                .orElseThrow(() -> new DeliveryLineDoesNotExistException("Delivery Line with ID of " + uid + " does not exist"));
        deliveryLineService.deleteDeliveryLine(deliveryLine);
        return "Delivery Line with ID of " + uid + " deleted";
    }

    /***
     * Create a new delivery line
     * @param request - the details of the delivery line
     * @return - a {@code CreateDeliveryLineResponse} containing the new delivery line
     */
    @PostMapping("/")
    public CreateDeliveryLineResponse createDeliveryLine(@RequestBody @Valid CreateDeliveryLineRequest request) {
        log.info("post: /api/delivery-line/ called");
        var orderLine = purchaseOrderLineService.getPurchaseOrderLineByUid(request.getPurchaseOrderLineId())
                .orElseThrow(() -> new PurchaseOrderLineDoesNotExistException("Purchase Order Line does not exist"));
        var delivery = deliveryService.getDeliveryByUid(request.getDeliveryId())
                .orElseThrow(() -> new DeliveryDoesNotExistException("Delivery does not exist"));
        var stockTransaction = stockTransactionService.getStockTransactionByUid(request.getStockTransactionId())
                .orElseThrow(() -> new StockTransactionDoesNotExistException("Stock Transaction does not exist"));

        var deliveryLine = mapper.mapFromCreateRequest(request, orderLine, delivery, stockTransaction);
        deliveryLine = deliveryLineService.save(deliveryLine);

        return mapper.mapToCreateResponse(deliveryLine);
    }

    /***
     * Update a delivery line
     * @param uid - the unique identifier of the delivery line to update
     * @param request - the details of the delivery line to update
     * @return - a {@code UpdateDeliveryResponse} containg the updated delivery line
     */
    @PutMapping("/{uid}")
    public UpdateDeliveryLineResponse updateDeliveryLine(@PathVariable @ValidUUID(message = "Delivery Line Id must be a UUID") String uid,
                                                         @RequestBody @Valid UpdateDeliveryLineRequest request) {
        log.info("post: /api/delivery-line/{} called", uid);
        var deliveryLine = deliveryLineService.getDeliveryLineByUid(uid)
                .orElseThrow(() -> new DeliveryLineDoesNotExistException("Delivery Line Does not exist"));
        mapper.updateFromUpdateRequest(deliveryLine, request);
        if(!deliveryLine.getPurchaseOrderLine().getUid().toString().equals(request.getPurchaseOrderLineId())) {
            var orderLine = purchaseOrderLineService.getPurchaseOrderLineByUid(request.getPurchaseOrderLineId())
                    .orElseThrow(() -> new PurchaseOrderLineDoesNotExistException("Purchase Order Line does not exist"));
            deliveryLine.setPurchaseOrderLine(orderLine);
        }
        if(!deliveryLine.getDelivery().getUid().toString().equals(request.getDeliveryId())) {
            var delivery = deliveryService.getDeliveryByUid(request.getDeliveryId())
                    .orElseThrow(() -> new DeliveryDoesNotExistException("Delivery does not exist"));
            deliveryLine.setDelivery(delivery);
        }
        if(!deliveryLine.getStockTransaction().getUid().toString().equals(request.getStockTransactionId())) {
            var stockTransaction = stockTransactionService.getStockTransactionByUid(request.getStockTransactionId())
                    .orElseThrow(() -> new StockTransactionDoesNotExistException("Stock Transaction does not exist"));
            deliveryLine.setStockTransaction(stockTransaction);
        }
        deliveryLine = deliveryLineService.save(deliveryLine);

        return mapper.mapToUpdateResponse(deliveryLine);
    }
}
