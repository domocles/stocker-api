package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.DomainObjectDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockTransactionDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.stocktransaction.*;
import com.eep.stocker.services.StockTransactionService;
import com.eep.stocker.services.StockableProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/09/2022
 *
 * Stock Transaction Controller
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/api/stock-transaction")
public class StockTransactionController {
    private final StockTransactionService stockTransactionService;
    private final StockableProductService stockableProductService;
    private final StockTransactionMapper mapper;

    /***
     * Get all stock transactions
     * @return a {@code GetAllStockTransactionsResponse} containing all stock transactions
     */
    @GetMapping("/")
    public GetAllStockTransactionsResponse getAllStockTransactions() {
        log.info("get: /api/stock-transaction/ called");
        var transactions = stockTransactionService.getAllStockTransactions();
        var response = new GetAllStockTransactionsResponse();
        transactions.stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response.allStockTransactions::add);
        return response;
    }

    /***
     * Get a stock transaction by its uid
     * @param uid - the uniqie identifier of the stock transaction to find
     * @return {@code GetStockTransactionResponse} containing the stock transaction
     */
    @GetMapping("/{uid}")
    public GetStockTransactionResponse getStockTransactionById(@PathVariable @ValidUUID(message = "Stock Transaction ID needs to be a UUID") String uid) {
        log.info("get: /api/stock-transaction/{}", uid);
        var stockTransaction = stockTransactionService.getStockTransactionByUid(uid)
                .orElseThrow(() -> new StockTransactionDoesNotExistException("Stock Transaction Does Not Exist"));

        return mapper.mapToGetResponse(stockTransaction);
    }

    /***
     * Get stock transaction for a stockable product
     * @param uid - the unique id of the stockable product to get the transactions for
     * @return a {@code GetStockTransactionsByStockableProductResponse} that contains all the transactions for a stockable product
     */
    @GetMapping("/stockable-product/{uid}/")
    public GetStockTransactionsByStockableProductResponse getStockTransactionsForStockableProduct(@PathVariable @ValidUUID(message = "Stockable Product ID needs to be a UUID") String uid) {
        log.info("get: /api/stock-transaction/stockable-product/{} called", uid);
        var stockableProduct = stockableProductService.getStockableProductByUid(uid)
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
        var response = new GetStockTransactionsByStockableProductResponse();
        var transactions = stockTransactionService.getAllStockTransactionsForStockableProduct(stockableProduct);
        transactions.stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response.allStockTransactions::add);

        return response;
    }

    /***
     * Get the balance of a stockable product
     * @param uid - the unique identifier of the stockable product to get the balance for
     * @return the balance of the stockable product
     */
    @GetMapping("/balance/{uid}")
    public Double getBalanceForStockableProduct(@PathVariable @ValidUUID(message = "Stockable Product ID needs to be a UUID") String uid) {
        log.info("get: /api/stock-transaction/balance/{} called", uid);
        var product = stockableProductService.getStockableProductByUid(uid)
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
        return stockTransactionService.getStockTransactionBalanceForStockableProduct(product);
    }

    /***
     * Create a new stock transaction from a {@code CreateStockTransactionRequest}
     * @param request - the request for the new stock transaction
     * @return
     */
    @PostMapping("/")
    public CreateStockTransactionResponse createStockTransaction(@RequestBody @Valid CreateStockTransactionRequest request) {
        log.info("post: /api/stock-transaction/ called");
        var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
        var newStockTransaction = mapper.mapCreateToStockTransaction(request, stockableProduct);
        newStockTransaction = stockTransactionService.saveStockTransaction(newStockTransaction);
        return mapper.mapToCreateResponse(newStockTransaction);
    }

    /***
     * Update a stock transaction from a {@code UpdateStockTransactionRequest}
     * @param uid - the unique identifier of the stock transaction to update
     * @param request - holds the updated values of the stock transaction
     * @return - a {@code UpdateStockTransactionResponse} with the updated Stock Transaction
     */
    @PutMapping("/{uid}")
    public UpdateStockTransactionResponse updateStockTransaction(@PathVariable @ValidUUID(message = "Stock Transaction ID needs to be a UUID") String uid, @RequestBody @Valid UpdateStockTransactionRequest request) {
        log.info("put: /api/stock-transaction/{} called", uid);
        var stockTransaction = stockTransactionService.getStockTransactionByUid(uid)
                .orElseThrow(() -> new StockTransactionDoesNotExistException("Stock Transaction Does Not Exist"));
        mapper.updateFromRequest(stockTransaction, request);
        if(!request.getStockableProductId().equals(stockTransaction.getStockableProduct().getUid().toString())) {
            var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                    .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
            stockTransaction.setStockableProduct(stockableProduct);
        }
        return mapper.mapToUpdateResponse(stockTransaction);
    }

    /***
     * Delete a stock transaction by its unique id
     * @param uid - the unique identifier of the stock transaction
     * @return a message confirming that the stock transaction has been deleted
     */
    @DeleteMapping("/{uid}")
    public String deleteStockTransaction(@PathVariable @ValidUUID(message = "Stock Transaction ID needs to be a UUID") String uid) {
        log.info("delete: /api/stock-transaction/{} called", uid);
        var transaction = stockTransactionService.getStockTransactionByUid(uid)
                .orElseThrow(() -> new StockTransactionDoesNotExistException("StockTransaction with id of " + uid + " does not exist"));
        stockTransactionService.deleteStockTransaction(transaction);
        return String.format("Stock Transaction with id of %s has been deleted", uid);
    }
}
