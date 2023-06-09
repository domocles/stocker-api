package com.eep.stocker.repository;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDeliveryRepository extends CrudRepository<Delivery, Long> {
    List<Delivery> findAll();
    List<Delivery> findAllBySupplier(Supplier supplier);
    List<Delivery> findAllByDeliveryDateBetween(LocalDate first, LocalDate second);
    Optional<Delivery> findByUid(UUID uid);
}
