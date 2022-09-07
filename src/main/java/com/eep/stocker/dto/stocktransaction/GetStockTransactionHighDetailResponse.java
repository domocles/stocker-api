package com.eep.stocker.dto.stocktransaction;

import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * High detail response with all domain objects fully included
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStockTransactionHighDetailResponse implements IStockTransactionDTO.Id,
        IStockTransactionDTO.StockableProduct,
        IStockTransactionDTO.Quantity,
        IStockTransactionDTO.Note,
        IStockTransactionDTO.Reference {
    private String id;
    private GetStockableProductResponse stockableProduct;
    private Double quantity;
    private String note;
    private String reference;
}
