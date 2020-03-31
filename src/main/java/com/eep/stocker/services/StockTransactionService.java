package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IDeliveryLineRepository;
import com.eep.stocker.repository.IStockTransactionRepository;
import com.eep.stocker.repository.IStockableProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockTransactionService {
    private static final Logger log = LoggerFactory.getLogger(StockTransactionService.class);

    private IStockTransactionRepository stockTransactionRepository;
    private IStockableProductRepository stockableProductRepository;
    private IDeliveryLineRepository deliveryLineRepository;

    public StockTransactionService(IStockTransactionRepository stockTransactionRepository,
                                   IStockableProductRepository stockableProductRepository,
                                   IDeliveryLineRepository deliveryLineRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
        this.stockableProductRepository = stockableProductRepository;
        this.deliveryLineRepository = deliveryLineRepository;
    }

    public List<StockTransaction> getAllStockTransactions() {
        log.info("getAllStockTransactions called");
        return stockTransactionRepository.findAll();
    }

    public Optional<StockTransaction> getStockTransactionById(long id) {
        log.info("getStockTransactionById called " + id);
        return stockTransactionRepository.findById(id);
    }

    public List<StockTransaction> getAllStockTransactionsForStockableProduct(StockableProduct stockableProduct) {
        log.info("getAllStockTransactionsForStockableProduct called");
        List<StockTransaction> stockTransactions = stockTransactionRepository.findAllByStockableProduct(stockableProduct);
        return stockTransactions;
    }

    public StockTransaction deleteStockTransaction(StockTransaction stockTransaction) {
        log.info("deleteStockTransaction called " + stockTransaction);
        stockTransactionRepository.delete(stockTransaction);
        return stockTransaction;
    }

    public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
        log.info("saveStockTransaction called " + stockTransaction);
        return stockTransactionRepository.save(stockTransaction);
    }

    public void saveStockTransactions(StockTransaction... stockTransactions) {
        stockTransactionRepository.saveAll(Arrays.asList(stockTransactions));
    }

    public void saveStockTransactions(Iterable<StockTransaction> stockTransactions) {
        stockTransactionRepository.saveAll(stockTransactions);
    }

    public double getStockTransactionBalanceForStockableProduct(StockableProduct stockableProduct) {
        Optional<Double> sum = stockTransactionRepository.getSumOfStockTransactionsForStockableProduct(stockableProduct);
        if(sum.isPresent()) {
            return sum.get();
        }
        return 0.0;
    }

    public double getBalanceForPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        Optional<Double> sum = deliveryLineRepository.getSumOfDeliveriesForPurchaseOrderLine(purchaseOrderLine);
        if(sum.isPresent()) {
            return sum.get();
        }
        return 0.0;
    }

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
}
