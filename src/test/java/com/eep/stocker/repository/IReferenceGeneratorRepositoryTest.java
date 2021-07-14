package com.eep.stocker.repository;

import com.eep.stocker.domain.ReferenceGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class IReferenceGeneratorRepositoryTest {
    @Autowired
    private IReferenceGeneratorRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private ReferenceGenerator deliveryReferenceGenerator;
    private ReferenceGenerator purchaseOrderReferenceGenerator;

    @BeforeEach
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
