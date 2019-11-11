package com.eep.stocker.services;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.IDeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class DeliveryServiceTest {
    @Mock
    private IDeliveryRepository deliveryRepository;

    private DeliveryService deliveryService;

    private Supplier shelleys;
    private Supplier ukf;
    private Delivery delivery1;
    private Delivery delivery2;
    private Delivery delivery3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.deliveryService = new DeliveryService(deliveryRepository);

        shelleys = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

        ukf = new Supplier("UKF Group Ltd",
                "GBP",
                "sales@ukf.com",
                "01527 578686");

        delivery1 = new Delivery();
        delivery1.setSupplier(shelleys);
        delivery1.setReference("12345");
        delivery1.setNote("Last minute delivery");

        delivery2 = new Delivery();
        delivery2.setSupplier(shelleys);
        delivery2.setReference("12345");
        delivery2.setNote("Last minute delivery 2");

        delivery3 = new Delivery();
        delivery3.setSupplier(ukf);
        delivery3.setReference("12345");
        delivery3.setNote("Last minute delivery 2");

    }

    @Test
    void getDeliveryByIdTest() {
        given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.of(delivery2));

        Optional<Delivery> delivery = this.deliveryService.getDeliveryById(1L);

        assertThat(delivery.isPresent()).isTrue();
        assertThat(delivery.get()).isEqualTo(delivery2);
    }

    @Test
    void getAllDeliveriesTest() {
        given(deliveryRepository.findAll()).willReturn(Arrays.asList(delivery1, delivery2));

        List<Delivery> deliveries = this.deliveryService.getAllDeliveries();

        assertThat(deliveries.size()).isEqualTo(2);
    }

    @Test
    void getAllDeliveriesForSupplierTest() {
        given(deliveryRepository.findAllBySupplier(shelleys)).willReturn(Arrays.asList(delivery1, delivery2));
        given(deliveryRepository.findAllBySupplier(ukf)).willReturn(Arrays.asList(delivery3));

        List<Delivery> shelleysDeliveries = deliveryService.getAllDeliveriesForSupplier(shelleys);
        List<Delivery> ukfDeliveries = deliveryService.getAllDeliveriesForSupplier(ukf);

        assertThat(shelleysDeliveries.size()).isEqualTo(2);
        assertThat(ukfDeliveries.size()).isEqualTo(1);
    }

    @Test
    void getAllDeliveriesByDatesTest() {
        given(deliveryRepository.findAllByDeliveryDateBetween(any(Date.class), any(Date.class)))
                .willReturn(Arrays.asList(delivery1, delivery2));

        List<Delivery> dateDeliveries = deliveryService.getAllDeliveriesBetween(new Date(), new Date());

        assertThat(dateDeliveries.size()).isEqualTo(2);
    }
}