package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.StockableProductDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierQuoteDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierQuoteErrorException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@RestController
public class SupplierQuoteController {
    private static Logger log = LoggerFactory.getLogger(SupplierQuoteController.class);

    private SupplierQuoteService supplierQuoteService;
    private SupplierService supplierService;
    private StockableProductService stockableProductService;

    public SupplierQuoteController(SupplierQuoteService supplierQuoteService, SupplierService supplierService,
                                   StockableProductService stockableProductService) {
        this.supplierQuoteService = supplierQuoteService;
        this.supplierService = supplierService;
        this.stockableProductService = stockableProductService;
    }

    @GetMapping("/api/supplier-quote/get")
    public List<SupplierQuote> getAllSupplierQuotes() {
        return supplierQuoteService.getAllSupplierQuotes();
    }

    @GetMapping("/api/supplier-quote/supplier/get/{id}")
    public List<SupplierQuote> getSupplierQuotesForSupplier(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierFromId(id);
        if(supplier.isPresent()) {
            return supplierQuoteService.getAllSupplierQuotesForSupplier(supplier.get());
        } else {
            throw new SupplierDoesNotExistException("Supplier with ID " + id + " does not exist");
        }
    }

    @GetMapping("/api/supplier-quote/stockable-product/{id}")
    public List<SupplierQuote> getSupplierQuotesForStockableProduct(@PathVariable Long id) {
        Optional<StockableProduct> stockableProduct =  stockableProductService.getStockableProductByID(id);
        if(stockableProduct.isPresent()) {
            return supplierQuoteService.getAllSupplierQuotesForStockableProduct(stockableProduct.get());
        } else {
            throw new StockableProductDoesNotExistException("Stockable Product with ID " + id + " doesn not exist");
        }
    }

    @GetMapping("/api/supplier-quote/get/{id}")
    public SupplierQuote getSupplierById(@PathVariable Long id) {
        Optional<SupplierQuote> quote = supplierQuoteService.getSupplierQuoteById(id);
        if(quote.isPresent()) {
            return quote.get();
        } else {
            throw new SupplierQuoteDoesNotExistException("Supplier Quote with id: " + id + " does not exist");
        }
    }

    @PostMapping(value = "/api/supplier-quote", consumes = "application/json", produces = "application/json")
    public SupplierQuote createNewSupplierQuote(@Valid @RequestBody SupplierQuote supplierQuote) {
        return supplierQuoteService.saveSupplierQuote(supplierQuote);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void supplierNotFoundHandler(SupplierDoesNotExistException ex) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void supplierQuoteErrorHandler(SupplierQuoteErrorException ex) {}

    @PostConstruct
    private void createSomeStockableQuotes() {
        Supplier tkrypp = new Supplier();
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

        fiftyMmTube = stockableProductService.saveStockableProduct(fiftyMmTube);

        SupplierQuote supplierQuote = new SupplierQuote();
        supplierQuote.setQuotationDate(new Date());
        supplierQuote.setStockableProduct(fiftyMmTube);
        supplierQuote.setSupplier(tkrypp);
        supplierQuote.setPrice(2.75D);
        supplierQuote.setQty(36.4);

        supplierQuoteService.saveSupplierQuote(supplierQuote);
    }
}
