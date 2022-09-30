package com.eep.stocker.dto.stockableproductnote;

import com.eep.stocker.annotations.validators.ValidUUID;
import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * 16/09/2022
 * A request DTO for the create stockable product note endpoint
 */
@Data
public class CreateStockableProductNoteRequest implements
        IStockableProductNoteDTO.StockableProductId,
        IStockableProductNoteDTO.Note {
    @ValidUUID(message = "Stockable Product ID needs to be a UUID")
    private String stockableProductId;
    private String note;
}
