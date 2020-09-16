package com.eep.stocker.services;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.IDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private IDeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(IDeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> getAllDeliveries() {
        return this.deliveryRepository.findAll();
    }

    public List<Delivery> getAllDeliveriesForSupplier(Supplier supplier) {
        log.info("GetAllDeliveriesForSuppler - {} called", supplier.getSupplierName());
        return deliveryRepository.findAllBySupplier(supplier);
    }

    public List<Delivery> getAllDeliveriesBetween(Date from, Date to) {
        log.info("GetAllDeliveriesForBetween - {} to {} called", from,  to);
        return deliveryRepository.findAllByDeliveryDateBetween(from, to);
    }

    public Optional<Delivery> getDeliveryById(Long id) {
        log.info("GetDeliveryById - {} called", id);
        return deliveryRepository.findById(id);
    }

    public Delivery saveDelivery(Delivery delivery) {
        log.info("SaveDelivery called");
        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(Delivery delivery) {
        log.info("DeleteDelivery called");
        deliveryRepository.delete(delivery);
    }
}
