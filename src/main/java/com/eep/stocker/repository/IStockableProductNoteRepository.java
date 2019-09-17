package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IStockableProductNoteRepository extends CrudRepository<StockableProductNote, Long> {
    List<StockableProductNote> findAllByStockableProduct_Id(Long id);
}
