package com.eep.stocker.dto.supplierquote;

import lombok.*;

import java.time.LocalDate;

/***
 * @author Sam Burns
 * @version 1.0
 * 09/09/2022
 *
 * Request DTO for the update supplier endpoint
 */

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierQuoteRequest implements
        ISupplierQuoteDTO.SupplierId,
        ISupplierQuoteDTO.StockableProductId,
        ISupplierQuoteDTO.Price,
        ISupplierQuoteDTO.Qty,
        ISupplierQuoteDTO.QuotationDate {
    private String supplierId;
    private String stockableProductId;
    private Double price;
    private Double qty;
    private LocalDate quotationDate;
}
