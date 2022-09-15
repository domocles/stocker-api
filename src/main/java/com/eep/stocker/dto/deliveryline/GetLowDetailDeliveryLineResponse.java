package com.eep.stocker.dto.deliveryline;


import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 13/09/2022
 * A standard response for the delivery line that only returns the id of domain objects.
 */
@Data
public class GetLowDetailDeliveryLineResponse implements IDeliveryLineDTO.Id,
        IDeliveryLineDTO.PurchaseOrderLineId,
        IDeliveryLineDTO.DeliveryId,
        IDeliveryLineDTO.StockTransactionId,
        IDeliveryLineDTO.QuantityDelivered,
        IDeliveryLineDTO.Note {
    private String id;
    private String purchaseOrderLineId;
    private String deliveryId;
    private String stockTransactionId;
    private Double quantityDelivered;
    private String note;
}

