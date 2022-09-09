package com.eep.stocker.dto.supplierquote;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetLowDetailSupplierQuoteResponse implements
        ISupplierQuoteDTO.Id,
        ISupplierQuoteDTO.StockableProductId,
        ISupplierQuoteDTO.SupplierId,
        ISupplierQuoteDTO.QuotationDate,
        ISupplierQuoteDTO.Qty,
        ISupplierQuoteDTO.Price {
    private String id;
    private String stockableProductId;
    private String supplierId;
    private LocalDate quotationDate;
    private Double qty;
    private Double price;
}
