package com.eep.stocker.services;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import com.eep.stocker.repository.IStockableProductNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockableProductNoteService {
    private static final Logger log = LoggerFactory.getLogger(StockableProductService.class);

    private IStockableProductNoteRepository stockableProductNoteRepository;

    public StockableProductNoteService(IStockableProductNoteRepository stockableProductNoteRepository) {
        this.stockableProductNoteRepository = stockableProductNoteRepository;
    }

    public List<StockableProductNote> getAllNotesForStockableProductId(Long id) {
        return stockableProductNoteRepository.findAllByStockableProduct_Id(id);
    }

    public StockableProductNote saveNote(StockableProductNote note) {
        return stockableProductNoteRepository.save(note);
    }

    public List<StockableProductNote> get() {
        return stockableProductNoteRepository.findAll();
    }

    public Optional<StockableProductNote> getById(long id) {
        return stockableProductNoteRepository.findById(id);
    }
}
