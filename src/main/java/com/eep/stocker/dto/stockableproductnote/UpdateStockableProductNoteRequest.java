package com.eep.stocker.dto.stockableproductnote;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 16/09/2022
 * Request DTO for the Update Stockable Product Note request
 */
@Data
public class UpdateStockableProductNoteRequest implements
        IStockableProductNoteDTO.StockableProductId,
        IStockableProductNoteDTO.Note {
    @ValidUUID(message = "Stockable Product ID needs to be a UUID")
    private String stockableProductId;
    private String note;
}
