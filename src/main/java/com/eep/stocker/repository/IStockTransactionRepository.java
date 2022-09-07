package com.eep.stocker.repository;

import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * Repository for Stock Transactions
 */
public interface IStockTransactionRepository extends CrudRepository<StockTransaction, Long> {
    List<StockTransaction> findAll();
    List<StockTransaction> findAllByStockableProduct(StockableProduct stockableProduct);
    Optional<StockTransaction> findByUid(UUID uid);

    @Query("SELECT SUM(s.quantity) FROM StockTransaction s WHERE s.stockableProduct = :sp")
    Optional<Double> getSumOfStockTransactionsForStockableProduct(StockableProduct sp);

    @Query("SELECT SUM(s.quantity) FROM StockTransaction s WHERE s.stockableProduct.id = :id")
    Optional<Double> getSumOfStockTransactionsForStockableProductById(Long id);
}
