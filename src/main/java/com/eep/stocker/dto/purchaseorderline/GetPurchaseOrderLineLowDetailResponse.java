package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPurchaseOrderLineLowDetailResponse implements IPurchaseOrderLineDTO.Id,
        IPurchaseOrderLineDTO.PurchaseOrderId,
        IPurchaseOrderLineDTO.StockableProductId,
        IPurchaseOrderLineDTO.Status,
        IPurchaseOrderLineDTO.Qty,
        IPurchaseOrderLineDTO.Price,
        IPurchaseOrderLineDTO.Balance,
        IPurchaseOrderLineDTO.Note {
    private String id;
    private String purchaseOrderId;
    private String stockableProductId;
    private Status status;
    private Double qty;
    private Double price;
    private Double balance;
    private String note;
}