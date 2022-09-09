package com.eep.stocker.dto.supplierquote;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSupplierQuoteRequest implements
        ISupplierQuoteDTO.SupplierId,
        ISupplierQuoteDTO.StockableProductId,
        ISupplierQuoteDTO.Qty,
        ISupplierQuoteDTO.Price,
        ISupplierQuoteDTO.QuotationDate {
    private String supplierId;
    private String stockableProductId;
    private Double qty;
    private Double price;
    private LocalDate quotationDate;
}
