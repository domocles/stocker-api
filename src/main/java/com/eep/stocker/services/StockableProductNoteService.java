package com.eep.stocker.services;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import com.eep.stocker.repository.IStockableProductNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockableProductNoteService {
    private static final Logger log = LoggerFactory.getLogger(StockableProductService.class);

    private IStockableProductNoteRepository stockableProductNoteRepository;

    public StockableProductNoteService(IStockableProductNoteRepository stockableProductNoteRepository) {
        this.stockableProductNoteRepository = stockableProductNoteRepository;
    }

    @Deprecated
    public List<StockableProductNote> getAllNotesForStockableProductId(Long id) {
        return stockableProductNoteRepository.findAllByStockableProduct_Id(id);
    }

    public StockableProductNote saveNote(StockableProductNote note) {
        return stockableProductNoteRepository.save(note);
    }

    public List<StockableProductNote> get() {
        return stockableProductNoteRepository.findAll();
    }

    @Deprecated
    public Optional<StockableProductNote> getById(long id) {
        return stockableProductNoteRepository.findById(id);
    }

    /***
     * Gets a stockable product note by its uid, note this will throw an IllegalArgumentException if the uuid is ill formatted
     * @param uid - the unique identifier of the stockable product note
     * @return an Optional containing the StockableProductNote if it exists else Optional.empty()
     */
    public Optional<StockableProductNote> getByUid(String uid) {
        var uuid = UUID.fromString(uid);
        return stockableProductNoteRepository.findByUid(uuid);
    }

    /***
     * Gets stockable product notes for a stockable product, note this will throw an IllegalArgumentException if the
     * uuid is ill formatted
     * @param uid - the unique identifier of the stockable product
     * @return a List containing all the StockableProductNotes for a stockable product, if none exist, will return an
     * empty list
     */
    public List<StockableProductNote> getAllNotesForStockableProductUid(String uid) {
        var uuid = UUID.fromString(uid);
        return stockableProductNoteRepository.findAllByStockableProduct_Uid(uuid);
    }
}
