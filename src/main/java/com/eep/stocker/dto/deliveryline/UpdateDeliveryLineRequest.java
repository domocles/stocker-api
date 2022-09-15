package com.eep.stocker.dto.deliveryline;

import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 15/09/2022
 * A request DTO for the update delivery line end point
 */
@Data
public class UpdateDeliveryLineRequest implements
        IDeliveryLineDTO.PurchaseOrderLineId,
        IDeliveryLineDTO.DeliveryId,
        IDeliveryLineDTO.StockTransactionId,
        IDeliveryLineDTO.QuantityDelivered,
        IDeliveryLineDTO.Note {
    private String purchaseOrderLineId;
    private String deliveryId;
    private String stockTransactionId;
    private Double quantityDelivered;
    private String note;
}
