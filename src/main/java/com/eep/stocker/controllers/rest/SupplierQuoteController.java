package com.eep.stocker.controllers.rest;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.controllers.error.exceptions.*;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.dto.supplierquote.*;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Date;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * Supplier Quote rest controller
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/supplier-quote")
public class SupplierQuoteController {
    private final SupplierQuoteService supplierQuoteService;
    private final SupplierService supplierService;
    private final StockableProductService stockableProductService;

    private final SupplierQuoteMapper mapper;

    /***
     * Get All Supplier Quotes
     * @return a {@code GetAllSupplierQuotesResponse} that contains all supplier quotes
     */
    @GetMapping("/")
    public GetAllSupplierQuotesResponse getAllSupplierQuotes() {
        log.info("get: /api/supplier-quote/ called");
        var response = new GetAllSupplierQuotesResponse();
        supplierQuoteService.getAllSupplierQuotes().stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addSupplierQuote);
        return response;
    }

    /***
     * Get all supplier quotes for a supplier
     * @param uid - the uniqie identifier of the supplier
     * @return a {@code GetSupplierQuotesForSupplierResponse} containing all the supplier quotes for the specified supplier
     */
    @GetMapping("/supplier/{uid}/")
    public GetSupplierQuotesForSupplierResponse getSupplierQuotesForSupplier(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/supplier-quote/supplier/" + uid + "/ called");
        var response = new GetSupplierQuotesForSupplierResponse();
        var supplier = supplierService.getSupplierFromUid(uid).orElseThrow(() -> new SupplierDoesNotExistException("Supplier does not exist"));
        supplierQuoteService.getAllSupplierQuotesForSupplier(supplier).stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addSupplierQuote);
        return response;
    }

    /***
     * Get all supplier quotes for a stockable product
     * @param uid - the unique identifier of the stockable product
     * @return a {@code GetSupplerQuotesForStockableProductResponse} containing all the supplier quotes for the specified stockable product
     */
    @GetMapping("/stockable-product/{uid}/")
    public GetSupplierQuotesForStockableProductResponse getSupplierQuotesForStockableProduct(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/supplier-quote/stockable-product/" + uid + "/ called");
        var stockableProduct =  stockableProductService.getStockableProductByUid(uid)
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
        var response = new GetSupplierQuotesForStockableProductResponse();
        supplierQuoteService.getAllSupplierQuotesForStockableProduct(stockableProduct).stream()
                .map(mapper::mapToLowDetailResponse)
                .forEach(response::addSupplierQuote);
        return response;
    }

    /***
     * Find supplier quote by its unique identifier
     * @param uid the unique identifier
     * @return a {@code GetSupplierQuoteResponse} containing the supplier quote
     */
    @GetMapping("/{uid}")
    public GetSupplierQuoteResponse getSupplierById(@PathVariable @ValidUUID String uid) {
        log.info("get: /api/supplier-quote/{} called", uid);
        var quote = supplierQuoteService.getSupplierQuoteByUid(uid)
                .orElseThrow(() -> new SupplierQuoteDoesNotExistException("Supplier Quote Does Not Exist"));
        return mapper.mapToGetResponse(quote);
    }

    /***
     * Delete a supplier quote based on its uniqie identifier
     * @param uid the unique identifier
     * @return a {@code DeleteSupplierQuoteResponse} containing the deleted supplier quote
     */
    @DeleteMapping("/{uid}")
    public DeleteSupplierQuoteResponse deleteSupplierQuoteById(@PathVariable @ValidUUID String uid) {
        log.info("delete: /api/supplier-quote/delete/{}", uid);
        var quote = supplierQuoteService.deleteSupplierQuoteByUid(uid)
                .orElseThrow(() -> new SupplierQuoteDoesNotExistException("Supplier quote does not exist"));
        return mapper.mapToDeleteResponse(quote);
    }

    /***
     * Create a new supplier quote based upon a {@code CreateSupplierQuoteRequest} request
     * @param request - the request
     * @return a {@code CreateSupplierQuoteResponse} containing the new SupplierQuote
     */
    @PostMapping(value = "/")
    public CreateSupplierQuoteResponse createNewSupplierQuote(@Valid @RequestBody CreateSupplierQuoteRequest request) {
        log.info("post: /api/supplier-quote/ called");
        var supplier = supplierService.getSupplierFromUid(request.getSupplierId())
                .orElseThrow(() -> new SupplierDoesNotExistException("Supplier Does Not Exist"));
        var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
        var supplierQuote = mapper.mapFromCreateRequest(request, supplier, stockableProduct);
        supplierQuote = supplierQuoteService.saveSupplierQuote(supplierQuote);
        return mapper.mapToCreateResponse(supplierQuote);
    }

    /***
     * Update a supplier quote
     * @param uid the unique id of the supplier quote to update
     * @param request the request
     * @return a {@code UpdateSupplierQuoteResponse} containing the updated SupplierQuote
     */
    @PutMapping(value = "/{uid}")
    public UpdateSupplierQuoteResponse updateSupplierQuote(@PathVariable @ValidUUID String uid, @RequestBody UpdateSupplierQuoteRequest request) {
        log.info("put: /api/supplier-quote/{} called", uid);
        var supplierQuote = supplierQuoteService.getSupplierQuoteByUid(uid)
                .orElseThrow(() -> new SupplierQuoteDoesNotExistException("Suppler Quote Does Not Exist"));
        if(!supplierQuote.getSupplier().getUid().toString().equals(request.getSupplierId())) {
            var supplier = supplierService.getSupplierFromUid(request.getSupplierId()).orElseThrow(() -> new SupplierDoesNotExistException("Supplier Does Not Exist"));
            supplierQuote.setSupplier(supplier);
        }
        if(!supplierQuote.getStockableProduct().getUid().toString().equals(request.getSupplierId())) {
            var stockableProduct = stockableProductService.getStockableProductByUid(request.getStockableProductId())
                    .orElseThrow(() -> new StockableProductDoesNotExistException("Stockable Product Does Not Exist"));
            supplierQuote.setStockableProduct(stockableProduct);
        }
        mapper.updateFromUpdateRequest(supplierQuote, request);
        supplierQuote = supplierQuoteService.saveSupplierQuote(supplierQuote);
        return mapper.mapToUpdateResponse(supplierQuote);
    }

    @PostConstruct
    private void createSomeStockableQuotes() {
        /*Supplier tkrypp = new Supplier();
        tkrypp.setSupplierName("Thyssen Krypp");
        tkrypp.setEmailAddress("Greg.Anderson@thyssenkrupp.com");
        tkrypp.setTelephoneNumber("01384 563123");

        tkrypp = supplierService.saveSupplier(tkrypp);

        StockableProduct fiftyMmTube = new StockableProduct();
        fiftyMmTube.setName("50.8 x 1.5mm T304 Stainless Steel Tube");
        fiftyMmTube.setUnits("Meters");
        fiftyMmTube.setStockPrice(2.5D);
        fiftyMmTube.setInStock(12.2D);
        fiftyMmTube.setDescription("Stainless steel tube, 6.1 meter lengths, 2\" OD.  Seam welded.");
        fiftyMmTube.setCategory("Tube");
        fiftyMmTube.setMpn("EEP200919001");

        try {
            fiftyMmTube = stockableProductService.saveStockableProduct(fiftyMmTube);

            SupplierQuote supplierQuote = new SupplierQuote();
            supplierQuote.setQuotationDate(new Date());
            supplierQuote.setStockableProduct(fiftyMmTube);
            supplierQuote.setSupplier(tkrypp);
            supplierQuote.setPrice(2.75D);
            supplierQuote.setQty(36.4);

            supplierQuoteService.saveSupplierQuote(supplierQuote);
        } catch(MpnNotUniqueException ex) {
            log.error(ex.getLocalizedMessage());
        }*/
    }
}
