package com.eep.stocker.dto.deliveryline;

import com.eep.stocker.dto.delivery.GetDeliveryResponse;
import com.eep.stocker.dto.purchaseorderline.GetPurchaseOrderLineResponse;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionResponse;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 13/09/2022
 *
 * High detail response DTO for a Delivery Line
 */
@Data
public class GetHighDetailDeliveryLineResponse implements IDeliveryLineDTO.Id,
        IDeliveryLineDTO.PurchaseOrderLine,
        IDeliveryLineDTO.Delivery,
        IDeliveryLineDTO.StockTransaction,
        IDeliveryLineDTO.QuantityDelivered,
        IDeliveryLineDTO.Note {
    private String id;
    private GetPurchaseOrderLineResponse purchaseOrderLine;
    private GetDeliveryResponse delivery;
    private GetStockTransactionResponse stockTransaction;
    private Double quantityDelivered;
    private String note;
}
