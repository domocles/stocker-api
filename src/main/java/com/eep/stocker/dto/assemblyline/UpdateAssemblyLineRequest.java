package com.eep.stocker.dto.assemblyline;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 29/09/2022
 *
 * Request DTO for the update assembly line end point
 */
@Data
public class UpdateAssemblyLineRequest implements AssemblyLineDTO.AssemblyId,
AssemblyLineDTO.StockableProductId, AssemblyLineDTO.Qty {
    @ValidUUID
    private String assemblyId;

    @ValidUUID
    private String stockableProductId;

    private Double qty;
}
