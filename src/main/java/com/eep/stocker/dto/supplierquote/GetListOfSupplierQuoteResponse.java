package com.eep.stocker.dto.supplierquote;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 08/09/2022
 * Response DTO that has a list of low detail responses
 */
@Data
public class GetListOfSupplierQuoteResponse {
    public final List<GetLowDetailSupplierQuoteResponse> supplierQuotes = new ArrayList<>();

    public void addSupplierQuote(GetLowDetailSupplierQuoteResponse supplierQuote) {
        this.supplierQuotes.add(supplierQuote);
    }
}
