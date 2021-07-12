package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.controllers.error.exceptions.RecordNotFoundException;
import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.stockableproduct.*;
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
import java.util.stream.Collectors;

@RestController
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final StockableProductService stockableProductService;

    private final StockableProductNoteService stockableProductNoteService;

    private final StockableProductMapper stockableProductMapper;

    public HomeController(StockableProductService stockableProductService, StockableProductNoteService stockableProductNoteService,
                          StockableProductMapper stockableProductMapper) {
        this.stockableProductService = stockableProductService;
        this.stockableProductNoteService = stockableProductNoteService;
        this.stockableProductMapper = stockableProductMapper;
    }

    /***
     * Retrieve the StockableProduct with the given id
     * @param id of the stockable product we wish to retrieve
     * @return GetStockableProductResponse
     * @throws RecordNotFoundException if the stockable product does not exist
     */
    @GetMapping("/api/stockable-products/get/{id}")
    public GetStockableProductResponse getById(@PathVariable String id) {
        log.info("get: /api/stockable-products/get/{} called", id);
        Optional<StockableProduct> stockableProductOpt = stockableProductService.getStockableProductByUid(id);
        StockableProduct stockableProduct = stockableProductOpt.orElseThrow(()
                -> new RecordNotFoundException("Material ID: '" + id + "' does not exist"));
        return stockableProductMapper.stockableProductResponseFromStockableProduct(stockableProduct);
    }

    /***
     * Retrieve all stockable products
     * @return a list of stockable products
     */
    @GetMapping("/api/stockable-products/get")
    public GetAllStockableProductResponse getAllStockableProducts() {
        log.info("get: /api/stockable-products/get called");
        GetAllStockableProductResponse response = new GetAllStockableProductResponse();
        List<StockableProduct> allProducts = stockableProductService.getAllStockableProducts();
        allProducts.stream()
                .map(stockableProductMapper::stockableProductResponseFromStockableProduct)
                .forEach(response::addGetStockableProductResponse);
        return response;
    }

    /***
     * Retrieve all categories
     * @return a list of categories
     */
    @GetMapping("/api/stockable-products/categories")
    public GetAllCategoriesResponse getAllCategories() {
        log.info("get: /api/stockable-products/categories called");
        GetAllCategoriesResponse response = new GetAllCategoriesResponse();
        stockableProductService.getAllCategories().forEach(response::addCategory);
        return response;
    }

    /***
     * Create a new stockable product
     * @param createStockableProductRequest
     * @return the persisted stockable product
     */
    @PostMapping(path = "/api/stockable-products/create", consumes = "application/json", produces = "application/json")
    public CreateStockableProductResponse createStockableProduct(@Valid @RequestBody CreateStockableProductRequest createStockableProductRequest) {
        log.info("post: /api/stockable-products/create called");
        Optional<StockableProduct> sb = stockableProductService.findStockableProductByMpn(createStockableProductRequest.getMpn());
        if(sb.isPresent()) {
            log.info("MPN already exists: '{}'", createStockableProductRequest.getMpn());
            throw new MpnNotUniqueException(createStockableProductRequest.getMpn() + " already exists");
        }
        StockableProduct stockableProduct = stockableProductMapper.stockableProductFromCreateStockableProductRequest(createStockableProductRequest);
        stockableProduct = stockableProductService.saveStockableProduct(stockableProduct);
        return stockableProductMapper.createStockableProductResponseFromStockableProduct(stockableProduct);
    }

    /***
     * Update a stockable product
     * @param request
     * @return the updated stockable product
     */
    @PutMapping(path = "/api/stockable-products/update", consumes = "application/json", produces = "application/json")
    public UpdateStockableProductResponse updateStockableProduct(@Valid @RequestBody UpdateStockableProductRequest request) {
        log.info("put: /api/stockable-products/update called");
        Optional<StockableProduct> sbOpt = stockableProductService.getStockableProductByUid(request.getId());
        if(sbOpt.isPresent()) {
            StockableProduct sb = sbOpt.get();
            //if the mpn is changing, make sure it doesn't clash
            if(!sb.getMpn().equals(request.getMpn())) {
                Optional<StockableProduct> mpnCheckOpt = stockableProductService.findStockableProductByMpn(request.getMpn());
                if(mpnCheckOpt.isPresent())
                    throw new MpnNotUniqueException("You are updating the mpn on the stockable product to one that" +
                            "already exists");
            }
            stockableProductMapper.updateStockableProductFromDto(request, sb);
            sb = stockableProductService.updateStockableProduct(sb);
            UpdateStockableProductResponse response = stockableProductMapper.updateStockableResponseFromStockableProduct(sb);
            return response;
        } else
            throw new StockableProductDoesNotExistException("Stockable Product does not exist");
    }

    /***
     * Delete a stockable product
     * @param id - the id of the stockable product to delete
     * @return a response as to whether the stockable product has been deleted
     * @throws ResourceNotFoundException - if the id is not found
     */
    @DeleteMapping(path = "/api/stockable-products/delete/{id}")
    public Response deleteStockableProduct(@PathVariable String id) throws ResourceNotFoundException {
        log.info("delete: /api/stockable-products/delete/{} called", id);
        Optional<StockableProduct> stockableProduct = stockableProductService.getStockableProductByUid(id);
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
