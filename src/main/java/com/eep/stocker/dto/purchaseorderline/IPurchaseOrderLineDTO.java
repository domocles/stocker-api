package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.dto.purchaseorder.GetPurchaseOrderResponse;
import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;

/***
 * @author Sam Burns
 * @version 05/09/2022
 * interface that enforces DTO
 */
public interface IPurchaseOrderLineDTO {
    interface Id{ String getId(); }
    interface PurchaseOrderId{ String getPurchaseOrderId(); }
    interface PurchaseOrder{ GetPurchaseOrderResponse getPurchaseOrder(); }
    interface StockableProductId{ String getStockableProductId(); }
    interface StockableProduct{ GetStockableProductResponse getStockableProduct(); }
    interface Status{ com.eep.stocker.domain.Status getStatus(); }
    interface Qty { Double getQty(); }
    interface Price { Double getPrice(); }
    interface Balance { Double getBalance(); }
    interface Note { String getNote(); }
}
