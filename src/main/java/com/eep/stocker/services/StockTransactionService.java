package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IDeliveryLineRepository;
import com.eep.stocker.repository.IStockTransactionRepository;
import com.eep.stocker.repository.IStockableProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * Stock Transaction service for retrieving and updating stock transactions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockTransactionService {
    /***
     * Repository for stock transactions
     */
    private final IStockTransactionRepository stockTransactionRepository;

    /***
     * Repository for stockable products
     */
    private final IStockableProductRepository stockableProductRepository;

    /***
     * Repository for delivery lines
     */
    private final IDeliveryLineRepository deliveryLineRepository;

    /***
     * Get all stock transactions
     * @return a List of all stock transactions, an empty list if there are none
     */
    public List<StockTransaction> getAllStockTransactions() {
        log.info("getAllStockTransactions called");
        return stockTransactionRepository.findAll();
    }

    /***
     * Find a stock transaction by its database id
     * @param id - the database id of the stock transaction
     * @deprecated - all stock transactions should be referenced by its uid
     * @return - an Optional containing the stock transaction if it exists, else returns Optional.empty()
     */
    @Deprecated
    public Optional<StockTransaction> getStockTransactionById(long id) {
        log.info("getStockTransactionById called " + id);
        return stockTransactionRepository.findById(id);
    }

    /***
     * Finds all stock transactions for a specified {@code StockableProduct}
     * @param stockableProduct - the stockable product to get the transactions for
     * @return - a list of transactions, or an empty list if there are none
     */
    public List<StockTransaction> getAllStockTransactionsForStockableProduct(StockableProduct stockableProduct) {
        log.info("getAllStockTransactionsForStockableProduct called");
        return stockTransactionRepository.findAllByStockableProduct(stockableProduct);
    }

    /***
     * Deletes a stock transaction from the database
     * @param stockTransaction - the stock transaction to delete
     * @return - the deleted stock transaction
     */
    public StockTransaction deleteStockTransaction(StockTransaction stockTransaction) {
        log.info("deleteStockTransaction called " + stockTransaction);
        stockTransactionRepository.delete(stockTransaction);
        return stockTransaction;
    }

    /***
     * Persists the stock transaction to the database
     * @param stockTransaction - the stock transaction to persist
     * @return the persisted Stock Transaction
     */
    public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
        log.info("saveStockTransaction called " + stockTransaction);
        return stockTransactionRepository.save(stockTransaction);
    }

    /***
     * Persist a list of stock transactions to the database
     * @param stockTransactions - the list of transactions to persist
     */
    public void saveStockTransactions(StockTransaction... stockTransactions) {
        stockTransactionRepository.saveAll(Arrays.asList(stockTransactions));
    }

    /***
     * Persist a list of stock transactions to the database
     * @param stockTransactions - the list of transactions to persist
     */
    public void saveStockTransactions(Iterable<StockTransaction> stockTransactions) {
        stockTransactionRepository.saveAll(stockTransactions);
    }

    /***
     * Gets the stock balance for a specific stockable product
     * @param stockableProduct - the stockable product to find the stock balance for
     * @return - the balance of stock if it exists, else returns 0.0
     */
    public double getStockTransactionBalanceForStockableProduct(StockableProduct stockableProduct) {
        Optional<Double> sum = stockTransactionRepository.getSumOfStockTransactionsForStockableProduct(stockableProduct);
        if(sum.isPresent()) {
            return sum.get();
        }
        return 0.0;
    }

    /***
     * Gets the stock balance for all stockable products
     * @deprecated - returns the database id, all stockable product shoule be referenced by its uid
     * @return - A Map containing a mapping of database id to it's balance
     */
    @Deprecated
    public Map<Long, Double> getStockTransactionBalanceForAllStockableProducts() {
        List<Long> ids = stockableProductRepository.findAllIdsForStockableProduct();
        Map<Long, Double> stockValues = new HashMap<>();
        for(Long id : ids) {
            Optional<Double> stock = stockTransactionRepository.getSumOfStockTransactionsForStockableProductById(id);
            if(stock.isPresent()) {
                stockValues.put(id, stock.get());
            } else {
                stockValues.put(id, 0.0);
            }
        }

        return stockValues;
    }

    /***
     * Finds a stock transaction by its uid, if none exists will return Optional.empty()
     * @param uid - the unique identifier of the stock transaction
     * @return an Optional containing the stock transaction or Optional.empty()
     */
    public Optional<StockTransaction> getStockTransactionByUid(String uid) {
        return stockTransactionRepository.findByUid(UUID.fromString(uid));
    }
}
