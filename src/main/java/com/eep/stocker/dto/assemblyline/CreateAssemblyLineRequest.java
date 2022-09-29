package com.eep.stocker.dto.assemblyline;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 28/09/2022
 *
 * Request DTO for the create delivery line endpoint
 */
@Data
public class CreateAssemblyLineRequest implements
        AssemblyLineDTO.StockableProductId,
        AssemblyLineDTO.AssemblyId,
        AssemblyLineDTO.Qty {
    @ValidUUID
    private String stockableProductId;

    @ValidUUID
    private String assemblyId;

    private Double qty;
}
