package com.eep.stocker.dto.assemblyline;

import com.eep.stocker.dto.assembly.GetAssemblyResponse;
import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;

import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 28/09/2022
 *
 * DTO for an AssemblyLine
 */
public interface AssemblyLineDTO {
    interface Id { String getId(); }
    interface StockableProduct { GetStockableProductResponse getStockableProduct(); }
    interface StockableProductId { String getStockableProductId(); }
    interface Assembly { GetAssemblyResponse getAssembly(); }
    interface AssemblyId { String getAssemblyId(); }
    interface Qty { Double getQty(); }
}
