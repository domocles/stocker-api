package com.eep.stocker.dto.stockableproduct.composite;

import com.eep.stocker.dto.deliveryline.IDeliveryLineDTO;
import lombok.Data;

import java.math.BigDecimal;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/10/2022
 * A response DTO for the delivery line, it is a composite part of the stockable product full detail response and is not
 * intended to be returned without being part a composite part of another response DTO
 */
@Data
public class StockableProductDeliveryLineCompositeDTO implements IDeliveryLineDTO.Id,
        IDeliveryLineDTO.DeliveryId,
        IDeliveryLineDTO.StockTransactionId,
        IDeliveryLineDTO.QuantityDelivered,
        IDeliveryLineDTO.Note,
        IDeliveryLineDTO.Balance,
        IStockableProductCompositeDTO.PurchaseOrderLine {
    private String id;
    private String deliveryId;
    private String stockTransactionId;
    private Double quantityDelivered;
    private String note;
    private BigDecimal balance;
    private StockableProductPurchaseOrderLineCompositeDTO purchaseOrderLine;
}
