package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IStockTransactionRepository;
import com.eep.stocker.repository.IStockableProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockableProductService {
    private static final Logger log = LoggerFactory.getLogger(StockableProductService.class);

    private final IStockableProductRepository stockableProductRepository;

    private final IStockTransactionRepository stockTransactionRepository;

    public StockableProductService(IStockableProductRepository stockableProductRepository, IStockTransactionRepository stockTransactionRepository) {
        this.stockableProductRepository = stockableProductRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    public StockableProduct saveStockableProduct(StockableProduct stockableProduct) {
        log.info("saveStockableProduct called");
        Optional<StockableProduct> sb = this.stockableProductRepository.findFirstByMpn(stockableProduct.getMpn());
        sb.ifPresent(s -> {throw new MpnNotUniqueException(String.format("%s is not a unique mpn", s.getMpn()));});
        return this.stockableProductRepository.save(stockableProduct);
    }

    public StockableProduct updateStockableProduct(StockableProduct stockableProduct) {
        log.info("update stockable product called");
        try {
            return this.stockableProductRepository.save(stockableProduct);
        } catch(PersistenceException e) {
            throw new MpnNotUniqueException("Mpn not unique");
        }
    }

    public Optional<StockableProduct> findStockableProductByMpn(String mpn) {
        log.info("findStockableProductByMpn called {}", mpn);
        Optional<StockableProduct> stockableProduct = this.stockableProductRepository.findFirstByMpn(mpn);
        return stockableProduct.flatMap(this::updateStockOfStockableProduct);
    }

    public Optional<StockableProduct> getStockableProductByID(Long ID) {
        log.info("getStockableProductByID called");
        return updateStockOfStockableProduct(ID);
    }

    public Optional<StockableProduct> getStockableProductByUid(String uid) {
        log.info("getStockableProductByUid called");
        Optional<StockableProduct> stockableProduct = this.stockableProductRepository.findFirstByUid(UUID.fromString(uid));
        return stockableProduct.flatMap(this::updateStockOfStockableProduct);
    }

    private Optional<StockableProduct> updateStockOfStockableProduct(Long id) {
        Optional<StockableProduct> stockableProductOptional = this.stockableProductRepository.findById(id);
        if(stockableProductOptional.isPresent()) {
            return  updateStockOfStockableProduct(stockableProductOptional.get());
        }
        return Optional.empty();
    }

    private Optional<StockableProduct> updateStockOfStockableProduct(StockableProduct stockableProduct) {
        log.info("Getting stock of stockable product");
        Optional<Double> stock = stockTransactionRepository.getSumOfStockTransactionsForStockableProductById(stockableProduct.getId());
        if (stock.isPresent()) {
            stockableProduct.setInStock(stock.get());
        } else {
            stockableProduct.setInStock(0);
        }
        return Optional.of(stockableProduct);
    }

    public List<StockableProduct> getAllStockableProducts() {
        //get all the stockable products from the stockableProductRepository
        List<StockableProduct> stockableProducts = stockableProductRepository.findAll();
        //get the stock balance levels for each product and set stock quantity to it.
        for(StockableProduct stockableProduct : stockableProducts) {
            Optional<Double> stock = stockTransactionRepository.getSumOfStockTransactionsForStockableProductById(stockableProduct.getId());
            if(stock.isPresent()) {
                stockableProduct.setInStock(stock.get());
            } else {
                stockableProduct.setInStock(0);
            }
        }
        return this.stockableProductRepository.findAll();
    }

    public List<String> getAllCategories() {
        return this.stockableProductRepository.findDistinctCategories();
    }

    public void deleteStockableProduct(StockableProduct stockableProduct) {
        this.stockableProductRepository.delete(stockableProduct);
    }
}
