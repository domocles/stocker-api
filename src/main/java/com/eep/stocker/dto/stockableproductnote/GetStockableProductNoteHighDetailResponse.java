package com.eep.stocker.dto.stockableproductnote;

import com.eep.stocker.dto.stockableproduct.GetStockableProductResponse;
import lombok.Data;

/***
 * High detail response DTO for the stockable product note rest controller
 */
@Data
public class GetStockableProductNoteHighDetailResponse implements
        IStockableProductNoteDTO.Id,
        IStockableProductNoteDTO.StockableProduct,
        IStockableProductNoteDTO.Note {
    private String Id;
    private GetStockableProductResponse stockableProduct;
    private String note;
}
