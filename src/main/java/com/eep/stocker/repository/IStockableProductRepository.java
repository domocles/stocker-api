package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IStockableProductRepository extends CrudRepository<StockableProduct, Long> {
    Optional<StockableProduct> findFirstByMpn(String mpn);
    Optional<StockableProduct> findFirstByUid(UUID uid);
    List<StockableProduct> findAll();

    @Query("SELECT DISTINCT category FROM StockableProduct")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT id FROM StockableProduct ")
    List<Long> findAllIdsForStockableProduct();
}
