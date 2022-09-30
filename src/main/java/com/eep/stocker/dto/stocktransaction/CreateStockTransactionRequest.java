package com.eep.stocker.dto.stocktransaction;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * Request DTO for the create stock transaction end point
 */
@Data
public class CreateStockTransactionRequest implements IStockTransactionDTO.StockableProductId,
        IStockTransactionDTO.Quantity,
        IStockTransactionDTO.Reference,
        IStockTransactionDTO.Note {
    @ValidUUID(message = "Stockable Product ID needs to be a UUID")
    private String stockableProductId;
    private Double quantity;
    private String reference;
    private String note;
}
