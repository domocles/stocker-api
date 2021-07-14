package com.eep.stocker.repository;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
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

    Supplier shelleys;
    Supplier ukf;
    Delivery delivery1;
    Delivery delivery2;
    Delivery delivery3;

    @BeforeEach
    private void initialize(){
        shelleys = new Supplier();
        //shelleys.setId(1L);
        shelleys.setSupplierName("Shelley Parts Ltd");
        shelleys.setDefaultCurrency("GBP");
        shelleys.setEmailAddress("jon.horton@shelleyparts.co.uk");
        shelleys.setTelephoneNumber("01384 956541");

        ukf = new Supplier();
        ukf.setSupplierName("UKF Ltd");
        ukf.setDefaultCurrency("GBP");
        ukf.setEmailAddress("sales@ukf-group.com");
        ukf.setTelephoneNumber("01527 578686");

        delivery1 = new Delivery();
        delivery1.setSupplier(shelleys);
        delivery1.setReference("12345");
        delivery1.setNote("Last minute delivery");

        delivery2 = new Delivery();
        delivery2.setSupplier(ukf);
        delivery2.setReference("12345");
        delivery2.setNote("Last minute delivery 2");

        delivery3 = new Delivery();
        delivery3.setSupplier(shelleys);
        delivery3.setReference("12345");
        delivery3.setNote("Last minute delivery 3");

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
        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistAndFlush(ukf);
        delivery1 = entityManager.persistAndFlush(delivery1);
        delivery2 = entityManager.persistAndFlush(delivery2);

        List<Delivery> allDeliveries = repository.findAllBySupplier(shelleys);

        assertThat(allDeliveries.size()).isEqualTo(1);
        assertThat(allDeliveries).contains(delivery1);
    }

    @Test
    void findAllDeliveriesByDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -14);
        Date twoWeeksAgo = cal.getTime();
        cal.add(Calendar.DATE, 7);
        Date oneWeekAgo = cal.getTime();

        delivery1.setDeliveryDate(twoWeeksAgo);
        delivery2.setDeliveryDate(oneWeekAgo);

        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistAndFlush(ukf);
        delivery1 = entityManager.persistAndFlush(delivery1);
        delivery2 = entityManager.persistAndFlush(delivery2);
        delivery3 = entityManager.persistAndFlush(delivery3);

        List<Delivery> deliveriesBetween = repository.findAllByDeliveryDateBetween(twoWeeksAgo, oneWeekAgo);

        assertThat(deliveriesBetween.size()).isEqualTo(2);
    }
}