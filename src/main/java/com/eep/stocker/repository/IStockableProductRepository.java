package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IStockableProductRepository extends CrudRepository<StockableProduct, Long> {
    Optional<StockableProduct> findFirstByMpn(String mpn);
    List<StockableProduct> findAll();

    @Query("SELECT DISTINCT category FROM StockableProduct")
    List<String> findDistinctCategories();
}
