package com.eep.stocker.dto.assemblyline;

import com.eep.stocker.dto.assembly.GetAssemblyResponse;
import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 28/09/2022
 *
 * High detail response DTO for an Assembly Line
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class GetHighDetailAssemblyLineResponse implements
        AssemblyLineDTO.Id,
        AssemblyLineDTO.StockableProduct,
        AssemblyLineDTO.Assembly,
        AssemblyLineDTO.Qty {
    private String id;
    private GetStockableProductResponse stockableProduct;
    private GetAssemblyResponse assembly;
    private Double qty;
}
