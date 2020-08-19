package com.eep.stocker.services;

import com.eep.stocker.domain.ReferenceGenerator;
import com.eep.stocker.repository.IReferenceGeneratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class ReferenceGeneratorServiceTest {
    @Mock
    private IReferenceGeneratorRepository referenceGeneratorRepository;

    private ReferenceGeneratorService referenceGeneratorService;

    private ReferenceGenerator deliveryReferenceGenerator;
    private ReferenceGenerator purchaseOrderReferenceGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        referenceGeneratorService = new ReferenceGeneratorService(referenceGeneratorRepository);

        deliveryReferenceGenerator = new ReferenceGenerator();
        deliveryReferenceGenerator.setId(1L);
        deliveryReferenceGenerator.setReferenceName("DELIVERY");
        deliveryReferenceGenerator.setPrefix("D");
        deliveryReferenceGenerator.setNumber(10L);

        purchaseOrderReferenceGenerator = new ReferenceGenerator();
        purchaseOrderReferenceGenerator.setId(2L);
        purchaseOrderReferenceGenerator.setReferenceName("PURCHASE");
        purchaseOrderReferenceGenerator.setPrefix("PO");
        purchaseOrderReferenceGenerator.setNumber(15L);
    }

    @Test
    void createReferenceGeneratorForExistingGeneratorReturnsFalseTest() {
        given(referenceGeneratorRepository.findFirstByReferenceName("DELIVERY"))
                .willReturn(Optional.of(deliveryReferenceGenerator));

        boolean createRefGenSuccess = referenceGeneratorService.createReferenceGenerator("DELIVERY", "D", 1L);

        assertThat(createRefGenSuccess).isFalse();
    }

    @Test
    void createReferenceGeneratorReturnsTrue() {
        given(referenceGeneratorRepository.findFirstByReferenceName("MPN"))
                .willReturn(Optional.empty());

        boolean createRefGenSuccess = referenceGeneratorService.createReferenceGenerator("MPN", "EEP", 1L);

        assertThat(createRefGenSuccess).isTrue();
    }

    @Test
    void getNextReferenceTest() {
        given(referenceGeneratorRepository.findFirstByReferenceName("DELIVERY"))
                .willReturn(Optional.of(deliveryReferenceGenerator));
        given(referenceGeneratorRepository.findFirstByReferenceName("PURCHASE"))
                .willReturn(Optional.of(purchaseOrderReferenceGenerator));

        Optional<String> nextDeliveryRef = referenceGeneratorService.getNextReference("DELIVERY");
        Optional<String> nextPurchaseRef = referenceGeneratorService.getNextReference("PURCHASE");

        assertThat(nextDeliveryRef.get()).isEqualTo("D0000010");
        assertThat(nextPurchaseRef.get()).isEqualTo("PO0000015");
    }

    @Test
    void resetReferenceTest() {
        given(referenceGeneratorRepository.findFirstByReferenceName("DELIVERY"))
                .willReturn(Optional.of(deliveryReferenceGenerator));

        assertThat(deliveryReferenceGenerator.getNumber()).isEqualTo(10L);

        Optional<String> nextDeliveryRef = referenceGeneratorService.getNextReference("DELIVERY");

        assertThat(deliveryReferenceGenerator.getNumber()).isEqualTo(11L);
    }

}