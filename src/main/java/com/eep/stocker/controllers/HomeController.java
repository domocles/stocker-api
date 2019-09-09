package com.eep.stocker.controllers;

import com.eep.stocker.controllers.error.exceptions.RecordNotFoundException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.StockableProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

@RestController
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private StockableProductService stockableProductService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/api/stockable-products/get/{id}")
    public StockableProduct getById(@PathVariable Long id) {
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            return stockableProduct.get();
        } else {
            log.info("RecordNotFoundException on material '"+id+"'");
            throw new RecordNotFoundException("Material ID: '" + id + "' does not exist");
        }
    }

    @PostConstruct
    private void initialize() {
        StockableProduct flex51x100 = new StockableProduct();
        flex51x100.setName("FX51x101ILOK");
        flex51x100.setMpn("FX24");
        flex51x100.setCategory("Flex");
        flex51x100.setDescription("ilok lined flex, 51mm ID, 100mm long");
        flex51x100.setInStock(100.0D);
        flex51x100.setStockPrice(1.51D);
        flex51x100.getTags().add("Flex");
        flex51x100.getTags().add("ilok");

        this.stockableProductService.saveStockableProduct(flex51x100);

        StockableProduct flex51x150 = new StockableProduct();
        flex51x150.setName("FX51x152ILOK");
        flex51x150.setMpn("FX24");
        flex51x150.setCategory("Flex");
        flex51x150.setDescription("ilok lined flex, 51mm ID, 150mm long");
        flex51x150.setInStock(125.0D);
        flex51x150.setStockPrice(1.76D);
        flex51x150.getTags().add("Flex");
        flex51x150.getTags().add("ilok");

        try {
            this.stockableProductService.saveStockableProduct(flex51x150);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation exception! " + e.getMessage());
        }
    }

}
