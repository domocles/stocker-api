package com.eep.stocker.dto.supplierquote;

import com.eep.stocker.annotations.validators.ValidUUID;
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
    @ValidUUID(message = "Supplier ID has to be a UUID")
    private String supplierId;
    @ValidUUID(message = "Stockable Product ID has to be a UUID")
    private String stockableProductId;
    private Double qty;
    private Double price;
    private LocalDate quotationDate;
}
