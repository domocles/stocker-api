package com.eep.stocker.dto.stockableproductnote;

import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;

/***
 * @author Sam Burns
 * @version 1.0
 * 15/09/2022
 * DTO interface for the StockableProductNote
 */
public interface IStockableProductNoteDTO {
    interface Id { String getId(); }
    interface Note { String getNote(); }
    interface StockableProduct { GetStockableProductResponse getStockableProduct(); }
    interface StockableProductId { String getStockableProductId(); }
}
