package com.eep.stocker.repository;

import com.eep.stocker.domain.ReferenceGenerator;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IReferenceGeneratorRepositoryTest {
    @Autowired
    private IReferenceGeneratorRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private ReferenceGenerator deliveryReferenceGenerator;
    private ReferenceGenerator purchaseOrderReferenceGenerator;

    @Before
    public void setup() {
        deliveryReferenceGenerator = new ReferenceGenerator();
        deliveryReferenceGenerator.setReferenceName("DELIVERY");
        deliveryReferenceGenerator.setPrefix("D");
        deliveryReferenceGenerator.setNumber(10L);

        deliveryReferenceGenerator = testEntityManager.persistFlushFind(deliveryReferenceGenerator);

        purchaseOrderReferenceGenerator = new ReferenceGenerator();
        purchaseOrderReferenceGenerator.setReferenceName("PURCHASE");
        purchaseOrderReferenceGenerator.setPrefix("PO");
        purchaseOrderReferenceGenerator.setNumber(15L);

        purchaseOrderReferenceGenerator = testEntityManager.persistFlushFind(purchaseOrderReferenceGenerator);
    }

    @Test
    public void getReferenceGeneratorByNameTest() {
        Optional<ReferenceGenerator> referenceGeneratorOpt = repository.findFirstByReferenceName("PURCHASE");
        assertThat(referenceGeneratorOpt).isPresent();
        assertThat(referenceGeneratorOpt.get()).isEqualTo(purchaseOrderReferenceGenerator);
    }
}
