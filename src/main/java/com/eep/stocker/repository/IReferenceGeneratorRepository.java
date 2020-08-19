package com.eep.stocker.repository;

import com.eep.stocker.domain.ReferenceGenerator;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IReferenceGeneratorRepository extends CrudRepository<ReferenceGenerator, Long> {
    Optional<ReferenceGenerator> findFirstByReferenceName(String referenceName);
}
