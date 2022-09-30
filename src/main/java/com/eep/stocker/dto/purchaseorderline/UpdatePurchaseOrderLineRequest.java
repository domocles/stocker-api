package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/09/2022
 * Request DTO for updating Purchase Order Lines
 */
@Data
public class UpdatePurchaseOrderLineRequest implements
        IPurchaseOrderLineDTO.PurchaseOrderId,
        IPurchaseOrderLineDTO.StockableProductId,
        IPurchaseOrderLineDTO.Qty,
        IPurchaseOrderLineDTO.Price,
        IPurchaseOrderLineDTO.Note {
    @ValidUUID(message = "Purchase Order ID needs to e a UUID")
    String purchaseOrderId;

    @ValidUUID(message = "Stockable Product ID needs to e a UUID")
    String stockableProductId;

    Double qty;

    Double price;

    String note;
}
