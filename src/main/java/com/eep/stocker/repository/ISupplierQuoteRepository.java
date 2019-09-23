package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ISupplierQuoteRepository extends CrudRepository<SupplierQuote, Long> {
    List<SupplierQuote> findBySupplier(Supplier supplier);
    List<SupplierQuote> findByStockableProduct(StockableProduct stockableProduct);
    SupplierQuote findTopByStockableProductAndSupplierOrderByQuotationDateDesc(StockableProduct stockableProduct, Supplier supplier);
}
