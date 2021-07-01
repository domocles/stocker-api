package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.controllers.error.exceptions.RecordNotFoundException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.StockableProductNoteService;
import com.eep.stocker.services.StockableProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import javax.validation.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@RestController
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final StockableProductService stockableProductService;

    private final StockableProductNoteService stockableProductNoteService;

    public HomeController(StockableProductService stockableProductService, StockableProductNoteService stockableProductNoteService) {
        this.stockableProductService = stockableProductService;
        this.stockableProductNoteService = stockableProductNoteService;
    }

    /***
     * Retrieve the StockableProduct with the given id
     * @param id of the stockable product we wish to retrieve
     * @return the stockable product
     * @throws RecordNotFoundException if the stockable product does not exist
     */
    @GetMapping("/api/stockable-products/get/{id}")
    public StockableProduct getById(@PathVariable Long id) {
        log.info("get: /api/stockable-products/get/{} called", id);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            return stockableProduct.get();
        } else {
            log.info("RecordNotFoundException on StockableProduct '{}'", id);
            throw new RecordNotFoundException("Material ID: '" + id + "' does not exist");
        }
    }

    /***
     * Retrieve all stockable products
     * @return a list of stockable products
     */
    @GetMapping("/api/stockable-products/get")
    public List<StockableProduct> getAllStockableProducts() {
        log.info("get: /api/stockable-products/get called");
        return stockableProductService.getAllStockableProducts();
    }

    /***
     * Retrieve all categories
     * @return a list of categories
     */
    @GetMapping("/api/stockable-products/categories")
    public List<String> getAllCategories() {
        log.info("get: /api/stockable-products/categories called");
        return  stockableProductService.getAllCategories();
    }

    /***
     * Create a new stockable product
     * @param stockableProduct
     * @return the persisted stockable product
     */
    @PostMapping(path = "/api/stockable-products/create", consumes = "application/json", produces = "application/json")
    public StockableProduct createStockableProduct(@Valid @RequestBody StockableProduct stockableProduct) {
        log.info("post: /api/stockable-products/create called");
        //todo create DTO's for this.
        Optional<StockableProduct> sb = stockableProductService.findStockableProductByMpn(stockableProduct.getMpn());
        if(sb.isPresent()) {
            log.info("MPN already exists: '{}'", stockableProduct.getMpn());
            throw new MpnNotUniqueException(stockableProduct.getMpn() + " already exists");
        }
        return stockableProductService.saveStockableProduct(stockableProduct);
    }

    /***
     * Update a stockable product
     * @param stockableProduct
     * @return the updated stockable product
     */
    @PutMapping(path = "/api/stockable-products/update", consumes = "application/json", produces = "application/json")
    public StockableProduct updateStockableProduct(@Valid @RequestBody StockableProduct stockableProduct) {
        log.info("put: /api/stockable-products/update called");
        Optional<StockableProduct> sb = stockableProductService.findStockableProductByMpn(stockableProduct.getMpn());
        if(sb.isPresent()) {
            if(sb.get().getId() != stockableProduct.getId()) {
                throw new MpnNotUniqueException(stockableProduct.getMpn() + " already belongs to another product");
            }
        }
        return stockableProductService.updateStockableProduct(stockableProduct);
    }

    /***
     * Delete a stockable product
     * @param id - the id of the stockable product to delete
     * @return a response as to whether the stockable product has been deleted
     * @throws ResourceNotFoundException - if the id is not found
     */
    @DeleteMapping(path = "/api/stockable-products/delete/{id}")
    public Response deleteStockableProduct(@PathVariable Long id) throws ResourceNotFoundException {
        log.info("delete: /api/stockable-products/delete/{} called", id);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            stockableProductService.deleteStockableProduct(stockableProduct.get());
            return Response.status(204)
                    .entity("Stockable Product is deleted").build();
        } else {
            throw new ResourceNotFoundException(String.format("StockableProduct with id '%s' doesn't exist", id));
        }
    }

    @PostConstruct
    private void initialize() {
        /*StockableProduct flex51x100 = new StockableProduct();
        flex51x100.setName("FX51x101ILOK");
        flex51x100.setMpn("FX24");
        flex51x100.setCategory("Flex");
        flex51x100.setDescription("ilok lined flex, 51mm ID, 100mm long");
        flex51x100.setInStock(100.0D);
        flex51x100.setStockPrice(1.51D);
        flex51x100.getTags().add("Flex");
        flex51x100.getTags().add("ilok");
        flex51x100.setUnits("Flexes");

        try {
            flex51x100 = this.stockableProductService.saveStockableProduct(flex51x100);

            StockableProductNote note = new StockableProductNote();
            note.setNote("This is a note you know");
            note.setStockableProduct(flex51x100);

            StockableProductNote note2 = new StockableProductNote();
            note2.setNote("This is another note");
            note2.setStockableProduct(flex51x100);

            this.stockableProductNoteService.saveNote(note);
            this.stockableProductNoteService.saveNote(note2);
        } catch(MpnNotUniqueException e) {
            log.error(e.getLocalizedMessage());
        }*/
    }

}
