package com.eep.stocker.dto.stocktransaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * Low detail response with all domain objects fully included
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStockTransactionLowDetailResponse implements IStockTransactionDTO.Id,
    IStockTransactionDTO.StockableProductId,
    IStockTransactionDTO.Quantity,
    IStockTransactionDTO.Note,
    IStockTransactionDTO.Reference {
    private String id;
    private String stockableProductId;
    private Double quantity;
    private String note;
    private String reference;
}
