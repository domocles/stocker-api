package com.eep.stocker.services;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.repository.ISupplierQuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Deprecated
    public Optional<SupplierQuote> deleteSupplierQuoteById(Long id) {
        Optional<SupplierQuote> quote = getSupplierQuoteById(id);
        quote.ifPresent(q -> supplierQuoteRepository.delete(q));
        return quote;
    }

    /***
     * Find a supplier quote by its unique id
     * @param uid - the unique id of the supplier quote
     * @return an Optional containing the supplier quote else Optional.empty()
     */
    public Optional<SupplierQuote> getSupplierQuoteByUid(String uid) {
        var uidVal = UUID.fromString(uid);
        return supplierQuoteRepository.findByUid(uidVal);
    }

    /***
     * Delete a supplier quote by its unique id number
     * @param uid - the unqiue id of the supplier quote
     * @return an Optional containing the supplier quote or else Optional.empty()
     */
    public Optional<SupplierQuote> deleteSupplierQuoteByUid(String uid) {
        var quote = getSupplierQuoteByUid(uid);
        quote.ifPresent(supplierQuoteRepository::delete);
        return quote;
    }
}
