package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IStockableProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockableProductService {
    private static final Logger log = LoggerFactory.getLogger(StockableProductService.class);

    @Autowired
    private IStockableProductRepository stockableProductRepository;

    public StockableProduct saveStockableProduct(StockableProduct stockableProduct) {
        Optional<StockableProduct> sb = stockableProductRepository.findFirstByMpn(stockableProduct.getMpn());
        if(sb.isPresent()) {
            throw new MpnNotUniqueException(stockableProduct.getMpn() + " already exists");
        }
        return this.stockableProductRepository.save(stockableProduct);
    }

    public Optional<StockableProduct> getStockableProductByID(Long ID) {
        return this.stockableProductRepository.findById(ID);
    }
}
