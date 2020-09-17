package com.eep.stocker.repository;

import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAssemblyLineRepository extends CrudRepository<AssemblyLine, Long> {
    List<AssemblyLine> findAll();
    List<AssemblyLine> getAssemblyLineByStockableProduct(StockableProduct stockableProduct);
    List<AssemblyLine> getAssemblyLineByAssembly(Assembly assembly);
}
