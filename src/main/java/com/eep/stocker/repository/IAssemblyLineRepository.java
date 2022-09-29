package com.eep.stocker.repository;

import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAssemblyLineRepository extends CrudRepository<AssemblyLine, Long> {
    List<AssemblyLine> findAll();
    Optional<AssemblyLine> findByUid(UUID uuid);

    List<AssemblyLine> getAssemblyLineByStockableProduct(StockableProduct stockableProduct);
    List<AssemblyLine> getAssemblyLineByAssembly(Assembly assembly);
}
