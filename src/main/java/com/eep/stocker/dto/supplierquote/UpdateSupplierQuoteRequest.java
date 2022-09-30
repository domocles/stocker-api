package com.eep.stocker.dto.supplierquote;

import com.eep.stocker.annotations.validators.ValidUUID;
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
    @ValidUUID(message = "Supplier ID has to be a UUID")
    private String supplierId;
    @ValidUUID(message = "Stockable Product ID has to be a UUID")
    private String stockableProductId;
    private Double price;
    private Double qty;
    private LocalDate quotationDate;
}
