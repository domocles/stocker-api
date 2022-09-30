package com.eep.stocker.dto.stocktransaction;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * Request DTO for the Update Stock Transaction endpoint
 */
@Data
public class UpdateStockTransactionRequest implements IStockTransactionDTO.StockableProductId,
        IStockTransactionDTO.Quantity,
        IStockTransactionDTO.Reference,
        IStockTransactionDTO.Note {
    @ValidUUID(message = "Stockable Product ID needs to be a UUID")
    private String stockableProductId;
    private Double quantity;
    private String reference;
    private String note;
}
