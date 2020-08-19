package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
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
        log.info("get: /api/stock-transaction/get/{}", id);
        Optional<StockTransaction> stockTransaction = stockTransactionService.getStockTransactionById(id);
        if(stockTransaction.isPresent()) {
            return stockTransaction.get();
        } else {
            throw new StockTransactionDoesNotExistException("Stock transaction with ID of "+ id + " does not exist");
        }
    }

    @GetMapping("/api/stock-transaction/stockable-product/get/{id}")
    public List<StockTransaction> getStockTransactionsForStockableProduct(@PathVariable long id) {
        log.info("get: /api/stock-transaction/stockable-product/get/{} called", id);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);

        return stockableProduct.map(stockTransactionService::getAllStockTransactionsForStockableProduct)
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable product with id of " + id + " does not exist"));

    }

    @GetMapping("/api/stock-transaction/balance/{id}")
    public Double getBalanceForStockableProduct(@PathVariable long id) {
        log.info("get: /api/stock-transaction/balance/{} called", id);
        Optional<StockableProduct> product = stockableProductService.getStockableProductByID(id);
        if(product.isPresent()) {
            double balance = stockTransactionService.getStockTransactionBalanceForStockableProduct(product.get());
            return balance;
        } else {
            throw new StockableProductDoesNotExistException("Stockable product with id of " + id + " does not exist");
        }
    }

    @PostMapping("/api/stock-transaction/create")
    public StockTransaction createStockTransaction(@RequestBody @Valid StockTransaction stockTransaction) {
        log.info("post: /api/stock-transaction/post called");
        return stockTransactionService.saveStockTransaction(stockTransaction);
    }

    @PutMapping("/api/stock-transaction/update")
    public StockTransaction updateStockTransaction(@RequestBody @Valid StockTransaction stockTransaction) {
        log.info("post: /api/stock-transaction/post called");
        //todo throw a DomainObjectNotFound if the stock transaction does not exist
        return stockTransactionService.saveStockTransaction(stockTransaction);

    }

    @DeleteMapping("/api/stock-transaction/delete/{id}")
    public StockTransaction deleteStockTransaction(@PathVariable long id) {
        log.info("delete: /api/stock-transaction/delete/{} called", id);
        Optional<StockTransaction> transaction = stockTransactionService.getStockTransactionById(id);
        return transaction.orElseThrow(() -> new DomainObjectDoesNotExistException("StockTransaction with id of " + id + " does not exist"));
    }
}
