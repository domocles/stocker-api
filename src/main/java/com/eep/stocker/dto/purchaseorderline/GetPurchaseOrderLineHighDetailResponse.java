package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.domain.Status;
import com.eep.stocker.dto.purchaseorder.GetPurchaseOrderResponse;
import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/***
 * @author Sam Burns
 * @version 1.0
 * 05/09/2022
 * High detail version of the purchase order line response
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPurchaseOrderLineHighDetailResponse implements IPurchaseOrderLineDTO.Id,
        IPurchaseOrderLineDTO.PurchaseOrder,
        IPurchaseOrderLineDTO.StockableProduct,
        IPurchaseOrderLineDTO.Status,
        IPurchaseOrderLineDTO.Qty,
        IPurchaseOrderLineDTO.Price,
        IPurchaseOrderLineDTO.Balance,
        IPurchaseOrderLineDTO.Note {
    private String id;
    private GetPurchaseOrderResponse purchaseOrder;
    private GetStockableProductResponse stockableProduct;
    private Status status;
    private Double qty;
    private Double price;
    private Double balance;
    private String note;
}
