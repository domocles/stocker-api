package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IStockTransactionRepository;
import com.eep.stocker.repository.IStockableProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockableProductService {
    private static final Logger log = LoggerFactory.getLogger(StockableProductService.class);

    @Autowired
    private IStockableProductRepository stockableProductRepository;

    @Autowired
    private IStockTransactionRepository stockTransactionRepository;

    public StockableProduct saveStockableProduct(StockableProduct stockableProduct) {
        log.info("saveStockableProduct called");
        return this.stockableProductRepository.save(stockableProduct);
    }

    public Optional<StockableProduct> findStockableProductByMpn(String mpn) {
        return this.stockableProductRepository.findFirstByMpn(mpn);
    }

    public Optional<StockableProduct> getStockableProductByID(Long ID) {
        log.info("getStockableProductByID called");
        return this.stockableProductRepository.findById(ID);
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
