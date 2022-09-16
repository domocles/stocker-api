package com.eep.stocker.dto.stockableproductnote;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 15/09/2022
 * A standard response for the stockable product note that only returns the id of domain objects.  Used when a lot of
 * stockable product notes are going to be returned.
 */
@Data
public class GetStockableProductNotesListOfLowDetailResponse {
    private final List<GetStockableProductNoteLowDetailResponse> productNotes = new ArrayList<>();

    public void addNote(GetStockableProductNoteLowDetailResponse note) {
        this.productNotes.add(note);
    }
}
