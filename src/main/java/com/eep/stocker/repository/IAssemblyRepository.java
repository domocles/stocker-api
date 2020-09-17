package com.eep.stocker.repository;

import com.eep.stocker.domain.Assembly;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAssemblyRepository extends CrudRepository<Assembly, Long> {
    List<Assembly> findAll();
}
