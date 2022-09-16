package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IStockableProductNoteRepository extends CrudRepository<StockableProductNote, Long> {
    List<StockableProductNote> findAll();
    List<StockableProductNote> findAllByStockableProduct_Id(Long id);
    List<StockableProductNote> findAllByStockableProduct_Uid(UUID uid);

    Optional<StockableProductNote> findByUid(UUID uuid);
}
