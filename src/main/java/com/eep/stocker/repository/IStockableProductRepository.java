package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.repository.CrudRepository;

public interface IStockableProductRepository extends CrudRepository<StockableProduct, Long> {
}
