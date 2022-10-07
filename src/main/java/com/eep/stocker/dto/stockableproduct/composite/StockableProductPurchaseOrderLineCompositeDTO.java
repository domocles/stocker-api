package com.eep.stocker.dto.stockableproduct.composite;

import com.eep.stocker.domain.Status;
import com.eep.stocker.dto.purchaseorderline.IPurchaseOrderLineDTO;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 05/10/2022
 * A response DTO for a purchase order line.  It is not intended for this
 * DTO to ever be returned from an endpoint other than as a composite member of another response
 */
@Data
public class StockableProductPurchaseOrderLineCompositeDTO implements
        IPurchaseOrderLineDTO.StockableProductId,
        IPurchaseOrderLineDTO.PurchaseOrderId,
        IPurchaseOrderLineDTO.Id,
        IPurchaseOrderLineDTO.Qty,
        IPurchaseOrderLineDTO.Balance,
        IPurchaseOrderLineDTO.Price,
        IPurchaseOrderLineDTO.Note,
        IPurchaseOrderLineDTO.Status {
    private String id;
    private String stockableProductId;
    private String purchaseOrderId;
    private Double qty;
    private Double balance;
    private Double price;
    private String note;
    private Status status;
}
