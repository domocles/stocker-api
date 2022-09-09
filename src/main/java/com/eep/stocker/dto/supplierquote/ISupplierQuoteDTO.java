package com.eep.stocker.dto.supplierquote;

import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;
import com.eep.stocker.dto.supplier.GetSupplierResponse;

import java.time.LocalDate;

public interface ISupplierQuoteDTO {
    interface Id { String getId(); }
    interface StockableProduct { GetStockableProductResponse getStockableProduct(); }
    interface StockableProductId { String getStockableProductId(); }
    interface Supplier { GetSupplierResponse getSupplier(); }
    interface SupplierId { String getSupplierId(); }
    interface QuotationDate { LocalDate getQuotationDate(); }
    interface Qty { Double getQty(); }
    interface Price { Double getPrice(); }
}
