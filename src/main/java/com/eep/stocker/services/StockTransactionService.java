package com.eep.stocker.services;

import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IStockTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StockTransactionService {
    private static final Logger log = LoggerFactory.getLogger(StockTransactionService.class);

    private IStockTransactionRepository stockTransactionRepository;

    public StockTransactionService(IStockTransactionRepository stockTransactionRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
    }

    public List<StockTransaction> getAllStockTransactions() {
        return stockTransactionRepository.findAll();
    }

    public Optional<StockTransaction> getStockTransactionById(long id) {
        return stockTransactionRepository.findById(id);
    }

    public List<StockTransaction> getAllStockTransactionsForStockableProduct(StockableProduct stockableProduct) {
        List<StockTransaction> stockTransactions = stockTransactionRepository.findAllByStockableProduct(stockableProduct);
        return stockTransactions;
    }

    public StockTransaction deleteStockTransaction(StockTransaction stockTransaction) {
        stockTransactionRepository.delete(stockTransaction);
        return stockTransaction;
    }

    public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
        return stockTransactionRepository.save(stockTransaction);
    }

    public void saveStockTransactions(StockTransaction... stockTransactions) {
        stockTransactionRepository.saveAll(Arrays.asList(stockTransactions));
    }

    public void saveStockTransactions(Iterable<StockTransaction> stockTransactions) {
        stockTransactionRepository.saveAll(stockTransactions);
    }

    public int getStockTransactionBalanceForStockableProduct(StockableProduct stockableProduct) {
        Optional<Integer> sum = stockTransactionRepository.getSumOfStockTransactionsForStockableProduct(stockableProduct);
        if(sum.isPresent()) {
            return sum.get();
        }
        return 0;
    }
}
