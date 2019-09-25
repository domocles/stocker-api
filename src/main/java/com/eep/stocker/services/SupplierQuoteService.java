package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.SupplierQuoteErrorException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.repository.ISupplierQuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierQuoteService {
    private static final Logger log = LoggerFactory.getLogger(SupplierQuoteService.class);

    private ISupplierQuoteRepository supplierQuoteRepository;

    public SupplierQuoteService(ISupplierQuoteRepository supplierQuoteRepository) {
        this.supplierQuoteRepository = supplierQuoteRepository;
    }

    public SupplierQuote saveSupplierQuote(SupplierQuote quote) {
        return this.supplierQuoteRepository.save(quote);
    }

    public Optional<SupplierQuote> getSupplierQuoteById(Long id) {
        return this.supplierQuoteRepository.findById(id);
    }

    public List<SupplierQuote> getAllSupplierQuotesForSupplier(Supplier supplier) {
        return supplierQuoteRepository.findBySupplier(supplier);
    }

    public List<SupplierQuote> getAllSupplierQuotesForStockableProduct(StockableProduct stockableProduct) {
        return supplierQuoteRepository.findByStockableProduct(stockableProduct);
    }

    public SupplierQuote getLastSupplierQuoteForStockableProductAndSupplier(StockableProduct stockableProduct, Supplier supplier) {
        return supplierQuoteRepository.findTopByStockableProductAndSupplierOrderByQuotationDateDesc(stockableProduct, supplier);
    }

    public List<SupplierQuote> getAllSupplierQuotes() {
        return supplierQuoteRepository.findAll();
    }

    public SupplierQuote updateSupplierQuote(SupplierQuote supplierQuote) {
        return supplierQuoteRepository.save(supplierQuote);
    }
}
