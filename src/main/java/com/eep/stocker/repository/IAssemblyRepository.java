package com.eep.stocker.repository;

import com.eep.stocker.domain.Assembly;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IAssemblyRepository extends CrudRepository<Assembly, Long> {
    List<Assembly> findAll();

    Optional<Assembly> findAssemblyByMpn(String mpn);

    List<Assembly> findAssemblyByCategory(String category);
}
