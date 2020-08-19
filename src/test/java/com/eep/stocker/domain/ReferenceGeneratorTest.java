package com.eep.stocker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceGeneratorTest {
    private ReferenceGenerator referenceGenerator;

    @BeforeEach
    void setup() {
        referenceGenerator = new ReferenceGenerator();
        referenceGenerator.setId(1L);
        referenceGenerator.setReferenceName("DELIVERY");
        referenceGenerator.setPrefix("D");
        referenceGenerator.setNumber(5L);
    }

    @Test
    public void testGenerateReferenceReturnsCorrectFormat() {
        assertThat(referenceGenerator.generate()).isEqualTo("D0000005");
    }
}
