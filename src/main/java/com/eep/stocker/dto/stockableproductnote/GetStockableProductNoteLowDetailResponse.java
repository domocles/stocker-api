package com.eep.stocker.dto.stockableproductnote;

import lombok.Data;

/***
 * @author Sam Burns
 * @version 1.0
 * Low detail response DTO for the stockable product note rest controller
 */
@Data
public class GetStockableProductNoteLowDetailResponse implements
        IStockableProductNoteDTO.Id,
        IStockableProductNoteDTO.StockableProductId,
        IStockableProductNoteDTO.Note {
    private String id;
    private String stockableProductId;
    private String note;
}
