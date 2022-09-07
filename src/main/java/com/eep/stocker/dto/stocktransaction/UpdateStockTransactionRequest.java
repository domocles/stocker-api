package com.eep.stocker.dto.stocktransaction;

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
    private String stockableProductId;
    private Double quantity;
    private String reference;
    private String note;
}
