package com.eep.stocker.dto.deliveryline;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 14/09/2022
 * A request DTO for the create delivery line endpoint
 */
@Data
public class CreateDeliveryLineRequest implements IDeliveryLineDTO.PurchaseOrderLineId,
    IDeliveryLineDTO.DeliveryId,
    IDeliveryLineDTO.StockTransactionId,
    IDeliveryLineDTO.QuantityDelivered,
    IDeliveryLineDTO.Note {
    @ValidUUID(message = "Purchase Order Line Id must be a UUID")
    private String purchaseOrderLineId;

    @ValidUUID(message = "Delivery Id must be a UUID")
    private String deliveryId;

    @ValidUUID(message = "Stock Transaction Id must be a UUID")
    private String stockTransactionId;

    private Double quantityDelivered;

    private String note;
}
