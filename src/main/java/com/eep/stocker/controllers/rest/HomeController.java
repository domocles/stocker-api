package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.controllers.error.exceptions.RecordNotFoundException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import com.eep.stocker.services.StockableProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.List;
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
        log.info("get stockable products by id called: '"+id+"'");
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            return stockableProduct.get();
        } else {
            log.info("RecordNotFoundException on StockableProduct '"+id+"'");
            throw new RecordNotFoundException("Material ID: '" + id + "' does not exist");
        }
    }

    @GetMapping("/api/stockable-products/get")
    public List<StockableProduct> getAllStockableProducts() {
        log.info("get all stockable-products called");
        List<StockableProduct> allStockableProducts = stockableProductService.getAllStockableProducts();
        return allStockableProducts;
    }

    @GetMapping("/api/stockable-products/categories")
    public List<String> getAllCategories() {
        log.info("get all categories");
        List<String> categories = stockableProductService.getAllCategories();
        return categories;
    }

    @PostMapping(path = "/api/stockable-products/create", consumes = "application/json", produces = "application/json")
    public StockableProduct createStockableProduct(@Valid @RequestBody StockableProduct stockableProduct) {
        log.info("Saving Stockable Product: " + stockableProduct.toString());
        log.info("Checking the MPN doesn't already exist");
        Optional<StockableProduct> sb = stockableProductService.findStockableProductByMpn(stockableProduct.getMpn());
        if(sb.isPresent()) {
            log.info("MPN already exists: '" + stockableProduct.getMpn() + "'");
            throw new MpnNotUniqueException(stockableProduct.getMpn() + " already exists");
        }
        return stockableProductService.saveStockableProduct(stockableProduct);
    }

    @PutMapping(path = "/api/stockable-products/update", consumes = "application/json", produces = "application/json")
    public StockableProduct updateStockableProduct(@Valid @RequestBody StockableProduct stockableProduct) {
        log.info("Updating Stockable Product: " + stockableProduct.toString());
        log.info("Checking that mpn does not conflict with any other products");
        Optional<StockableProduct> sb = stockableProductService.findStockableProductByMpn(stockableProduct.getMpn());
        if(sb.isPresent()) {
            if(sb.get().getId() != stockableProduct.getId()) {
                throw new MpnNotUniqueException(stockableProduct.getMpn() + " already belongs to another product");
            }
        }
        return stockableProductService.saveStockableProduct(stockableProduct);
    }

    @DeleteMapping(path = "/api/stockable-products/delete/{id}")
    public Response deleteStockableProduct(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            stockableProductService.deleteStockableProduct(stockableProduct.get());
            return Response.status(204)
                    .entity("Stockable Product is deleted").build();
        } else {
            throw new ResourceNotFoundException("StockableProduct with id '" + id + "' doesn't exist");
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
        flex51x100.setUnits("Flexes");

        StockableProductNote note = new StockableProductNote();
        note.setNote("This is a note you know");
        flex51x100.addNote(note);
        StockableProductNote note2 = new StockableProductNote();
        note2.setNote("This is another note");
        flex51x100.addNote(note2);


        this.stockableProductService.saveStockableProduct(flex51x100);
    }

}
