package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.StockTransactionDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.StockTransactionService;
import com.eep.stocker.services.StockableProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class StockTransactionController {
    private static final Logger log = LoggerFactory.getLogger(StockTransactionController.class);

    private StockTransactionService stockTransactionService;
    private StockableProductService stockableProductService;

    @Autowired
    public StockTransactionController(StockTransactionService stockTransactionService,
                                      StockableProductService stockableProductService) {
        this.stockTransactionService = stockTransactionService;
        this.stockableProductService = stockableProductService;
    }

    @GetMapping("/api/stock-transaction/get")
    public List<StockTransaction> getAllStockTransactions() {
        log.info("get: /api/stock-transaction/get called");
        return stockTransactionService.getAllStockTransactions();
    }

    @GetMapping("/api/stock-transaction/get/{id}")
    public StockTransaction getStockTransactionById(@PathVariable long id) {
        log.info("get: /api/stock-transaction/get/" + id + " called");
        Optional<StockTransaction> stockTransaction = stockTransactionService.getStockTransactionById(id);
        if(stockTransaction.isPresent()) {
            return stockTransaction.get();
        } else {
            throw new StockTransactionDoesNotExistException("Stock transaction with ID of "+ id + " does not exist");
        }
    }

    @GetMapping("/api/stock-transaction/stockable-product/get/{id}")
    public List<StockTransaction> getStockTransactionsForStockableProduct(@PathVariable long id) {
        log.info("get: /api/stock-transaction/stockable-product/get" + id + " called");
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            List<StockTransaction> stockTransactions = stockTransactionService.getAllStockTransactionsForStockableProduct(stockableProduct.get());
            return stockTransactions;
        } else {
            throw new StockableProductDoesNotExistException("Stockable product with id of " + id + " does not exist");
        }
    }

    @PostMapping("/api/stock-transaction/post")
    public StockTransaction createStockTransaction(@RequestBody @Valid StockTransaction stockTransaction) {
        log.info("post: /api/stock-transaction/post called");
        StockTransaction transaction = stockTransactionService.saveStockTransaction(stockTransaction);
        return transaction;
    }

    @PutMapping("/api/stock-transaction/put")
    public StockTransaction updateStockTransaction(@RequestBody @Valid StockTransaction stockTransaction) {
        log.info("post: /api/stock-transaction/post called");
        StockTransaction transaction = stockTransactionService.saveStockTransaction(stockTransaction);
        return transaction;
    }

    @DeleteMapping("/api/stock-transaction/delete/{id}")
    public StockTransaction deleteStockTransaction(@PathVariable long id) {
        log.info("delete: /api/stock-transaction/delete/" + id + " called");
        Optional<StockTransaction> transaction = stockTransactionService.getStockTransactionById(id);
        if(transaction.isPresent()) {
            return stockTransactionService.deleteStockTransaction(transaction.get());
        } else {
            throw new StockTransactionDoesNotExistException("Stock transaction with id of " + id + " does not exist");
        }
    }
}
