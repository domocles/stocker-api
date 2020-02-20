package com.eep.stocker.repository;

import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IStockTransactionRepository extends CrudRepository<StockTransaction, Long> {
    List<StockTransaction> findAll();
    List<StockTransaction> findAllByStockableProduct(StockableProduct stockableProduct);

    @Query("SELECT SUM(s.quantity) FROM StockTransaction s WHERE s.stockableProduct = :sp")
    Optional<Integer> getSumOfStockTransactionsForStockableProduct(StockableProduct sp);
}
