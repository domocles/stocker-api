package com.eep.stocker.repository;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;

import com.eep.stocker.testdata.DeliveryTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class IDeliveryRepositoryTest {

    @Autowired
    private IDeliveryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Supplier shelleys;
    private Supplier ukf;
    private Delivery delivery1;
    private Delivery delivery2;
    private Delivery delivery3;

    @BeforeEach
    void setup() {
        shelleys = Supplier.builder()
                //.id(1L)
                .supplierName("Shelley Parts Ltd")
                .defaultCurrency("GBP")
                .emailAddress("jon.horton@shelleyparts.co.uk")
                .telephoneNumber("01527 584285")
                .build();
        ukf = Supplier.builder()
                //.id(2L)
                .supplierName("UKF Ltd")
                .defaultCurrency("GBP")
                .emailAddress("sales@ukf-group.com")
                .telephoneNumber("01527 578686")
                .build();
        delivery1 = Delivery.builder()
                //.id(1L)
                .deliveryDate(LocalDate.of(2021, 07, 15))
                .reference("testdelivery")
                .note("A test delivery")
                .supplier(shelleys)
                .build();
        delivery2 = Delivery.builder()
                //.id(2L)
                .deliveryDate(LocalDate.of(2021, 05, 15))
                .reference("testdelivery2")
                .note("A second test delivery")
                .supplier(ukf)
                .build();
        delivery3 = Delivery.builder()
                //.id(3L)
                .deliveryDate(LocalDate.of(2021, 8, 15))
                .reference("testdelivery3")
                .note("A third test delivery")
                .supplier(ukf)
                .build();
    }

    @Test
    void getDeliveryByIdTest() {
        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistAndFlush(ukf);
        delivery1 = entityManager.persistAndFlush(delivery1);

        Optional<Delivery> testDelivery = repository.findById(delivery1.getId());

        assertThat(testDelivery.isPresent()).isTrue();
        assertThat(shelleys.getId()).isGreaterThan(0);
    }

    @Test
    void getDeliveryByUidTest() {
        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistAndFlush(ukf);
        delivery1 = entityManager.persistAndFlush(delivery1);

        Optional<Delivery> testDelivery = repository.findByUid(delivery1.getUid());

        assertThat(testDelivery.isPresent()).isTrue();
        assertThat(shelleys.getId()).isGreaterThan(0);
    }

    @Test
    void findAllTest() {
        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistAndFlush(ukf);
        delivery1 = entityManager.persistAndFlush(delivery1);
        delivery2 = entityManager.persistAndFlush(delivery2);

        List<Delivery> allDeliveries = repository.findAll();

        assertThat(allDeliveries).isNotNull();
        assertThat(allDeliveries.size()).isEqualTo(2);

    }

    @Test
    void findAllBySupplierTest() {
        Supplier persistedShelleys = entityManager.persistFlushFind(shelleys);
        Supplier persistedUkf = entityManager.persistAndFlush(ukf);
        Delivery persistedDelivery1 = entityManager.persistAndFlush(delivery1);
        Delivery persistedDelivery2 = entityManager.persistAndFlush(delivery2);

        List<Delivery> allDeliveries = repository.findAllBySupplier(persistedShelleys);

        assertThat(allDeliveries.size()).isEqualTo(1);
        assertThat(allDeliveries).contains(delivery1);
    }

    @Test
    void findAllDeliveriesByDate() {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        delivery1.setDeliveryDate(LocalDate.now().minusDays(14));
        delivery2.setDeliveryDate(LocalDate.now().minusDays(7));

        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistAndFlush(ukf);
        delivery1 = entityManager.persistAndFlush(delivery1);
        delivery2 = entityManager.persistAndFlush(delivery2);
        delivery3 = entityManager.persistAndFlush(delivery3);

        List<Delivery> deliveriesBetween = repository.findAllByDeliveryDateBetween(twoWeeksAgo, oneWeekAgo);

        assertThat(deliveriesBetween.size()).isEqualTo(2);
    }
}