package com.eep.stocker.dto.stockableproduct.composite;

import com.eep.stocker.dto.supplier.GetSupplierResponse;
import com.eep.stocker.dto.supplierquote.ISupplierQuoteDTO;
import lombok.Data;

import java.time.LocalDate;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/10/2022
 * A composite response DTO for the supplier quote.  This is a composite DTO and is not intended to be directly returned
 * by an endpoint but a composite part of another DTO
 */
@Data
public class StockableProductSupplierQuoteCompositeDTO implements ISupplierQuoteDTO.Id,
        ISupplierQuoteDTO.StockableProductId,
        ISupplierQuoteDTO.Supplier,
        ISupplierQuoteDTO.QuotationDate,
        ISupplierQuoteDTO.Price,
        ISupplierQuoteDTO.Qty {
    private String id;
    private String stockableProductId;
    private GetSupplierResponse supplier;
    private LocalDate quotationDate;
    private Double price;
    private Double qty;
}
