package com.eep.stocker.repository;

import com.eep.stocker.domain.Assembly;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAssemblyRepository extends CrudRepository<Assembly, Long> {
    List<Assembly> findAll();

    Optional<Assembly> findByUid(UUID uid);

    Optional<Assembly> findAssemblyByMpn(String mpn);

    List<Assembly> findAssemblyByCategory(String category);

    @Query("SELECT DISTINCT category FROM assembly")
    List<String> findDistinctCategories();
}
