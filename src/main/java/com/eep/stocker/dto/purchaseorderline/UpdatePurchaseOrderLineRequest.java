package com.eep.stocker.dto.purchaseorderline;

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
    String purchaseOrderId;
    String stockableProductId;
    Double qty;
    Double price;
    String note;
}
