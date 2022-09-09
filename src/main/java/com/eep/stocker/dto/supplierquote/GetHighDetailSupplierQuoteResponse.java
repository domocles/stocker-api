package com.eep.stocker.dto.supplierquote;

import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;
import com.eep.stocker.dto.supplier.GetSupplierResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetHighDetailSupplierQuoteResponse implements
        ISupplierQuoteDTO.Id,
        ISupplierQuoteDTO.StockableProduct,
        ISupplierQuoteDTO.Supplier,
        ISupplierQuoteDTO.QuotationDate,
        ISupplierQuoteDTO.Qty,
        ISupplierQuoteDTO.Price {
    private String id;
    private GetStockableProductResponse stockableProduct;
    private GetSupplierResponse supplier;
    private LocalDate quotationDate;
    private Double qty;
    private Double price;
}
