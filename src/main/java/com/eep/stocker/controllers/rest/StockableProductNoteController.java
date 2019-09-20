package com.eep.stocker.controllers.rest;


import com.eep.stocker.domain.StockableProductNote;
import com.eep.stocker.services.StockableProductNoteService;
import com.eep.stocker.services.StockableProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StockableProductNoteController {
    private static final Logger log = LoggerFactory.getLogger(StockableProductNoteController.class);

    private final StockableProductService stockableProductService;
    private final StockableProductNoteService stockableProductNoteService;

    public StockableProductNoteController(StockableProductService stockableProductService,
                                          StockableProductNoteService stockableProductNoteService) {
        this.stockableProductService = stockableProductService;
        this.stockableProductNoteService = stockableProductNoteService;
    }

    @GetMapping("/api/stockable-product-note/stockable-product/get/{id}")
    public List<StockableProductNote> getAllNotesForStockableProduct(@PathVariable Long id) {
        return this.stockableProductNoteService.getAllNotesForStockableProductId(id);
    }

    @PostMapping(path = "/api/stockable-product-note/create", consumes = "application/json", produces = "application/json")
    public StockableProductNote saveNote(@Valid @RequestBody StockableProductNote note) {

        log.info("Saving note");
        return stockableProductNoteService.saveNote(note);
    }
}
