package com.eep.stocker.dto.deliveryline;

import com.eep.stocker.dto.delivery.GetDeliveryResponse;
import com.eep.stocker.dto.purchaseorderline.GetPurchaseOrderLineResponse;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionResponse;

/***
 * @author Sam Burns
 * @version 1.0
 * DTO interface for the DeliveryLine
 */
public interface IDeliveryLineDTO {
    interface Id { String getId(); }
    interface PurchaseOrderLineId { String getPurchaseOrderLineId(); }
    interface PurchaseOrderLine { GetPurchaseOrderLineResponse getPurchaseOrderLine(); }
    interface DeliveryId { String getDeliveryId(); }
    interface Delivery { GetDeliveryResponse getDelivery(); }
    interface StockTransactionId { String getStockTransactionId(); }
    interface StockTransaction { GetStockTransactionResponse getStockTransaction(); }
    interface QuantityDelivered { Double getQuantityDelivered(); }
    interface Note { String getNote(); }
}
