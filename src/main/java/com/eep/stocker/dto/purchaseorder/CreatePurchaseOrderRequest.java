package com.eep.stocker.dto.purchaseorder;

import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 25/08/2022
 * DTO for creating purchase orders.  Status and Date not included.  A new purchase order will have a status of OPEN.
 * The date will be set to the date that the order was created.
 */
@Data
public class CreatePurchaseOrderRequest implements
        IPurchaseOrderDTO.PurchaseOrderReference,
        IPurchaseOrderDTO.SupplierId {
    private String purchaseOrderReference;
    private String supplierId;
}
