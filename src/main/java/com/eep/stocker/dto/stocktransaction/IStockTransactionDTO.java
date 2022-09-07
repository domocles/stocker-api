package com.eep.stocker.dto.stocktransaction;

import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;

public interface IStockTransactionDTO {
    interface Id { String getId(); }
    interface StockableProduct { GetStockableProductResponse getStockableProduct(); }
    interface StockableProductId { String getStockableProductId(); }
    interface Quantity { Double getQuantity(); }
    interface Reference { String getReference(); }
    interface Note { String getNote();}
}
