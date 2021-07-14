package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.ReferenceGenerator;
import com.eep.stocker.services.ReferenceGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReferenceGeneratorControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ReferenceGeneratorService referenceGeneratorService;

    private ReferenceGenerator deliveryReferenceGenerator;
    private ReferenceGenerator purchaseOrderReferenceGenerator;

    @BeforeEach
    void setUp() {
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
    void getNewReferenceReturnsHttpStatusOkTest() {
        given(referenceGeneratorService.getNextReference("DELIVERY")).willReturn(Optional.of("D0000001"));

        ResponseEntity<String> response = restTemplate.exchange("/api/reference/DELIVERY", HttpMethod.GET, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getNewReferenceReturnsNewReferenceTest() {
        given(referenceGeneratorService.getNextReference("DELIVERY")).willReturn(Optional.of("D0000001"));
        given(referenceGeneratorService.getNextReference("PURCHASE")).willReturn(Optional.of("PO0000001"));

        ResponseEntity<String> deliveryResponse = restTemplate.exchange("/api/reference/DELIVERY", HttpMethod.GET, null, String.class);
        ResponseEntity<String> purchaseResponse = restTemplate.exchange("/api/reference/PURCHASE", HttpMethod.GET, null, String.class);

        assertThat(deliveryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deliveryResponse.getBody()).isEqualTo("D0000001");

        assertThat(purchaseResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(purchaseResponse.getBody()).isEqualTo("PO0000001");
    }

    @Test
    void getNewReferenceForNonExistantReferenceGeneratorThrowsExceptionText() {
        given(referenceGeneratorService.getNextReference(anyString())).willReturn(Optional.empty());

        ResponseEntity<String> deliveryResponse = restTemplate.exchange("/api/reference/DELIVERY", HttpMethod.GET, null, String.class);

        assertThat(deliveryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createNewReferenceGeneratorTestReturnsStatusOk() {
        given(referenceGeneratorService.createReferenceGenerator(anyString(), anyString(), anyLong())).willReturn(true);

        ResponseEntity<String> response = restTemplate.exchange("/api/reference/MPN/EEP", HttpMethod.POST, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createNewReferenceGeneratorReturnsUriTest() {
        given(referenceGeneratorService.createReferenceGenerator(anyString(), anyString(), anyLong())).willReturn(true);
        ResponseEntity<Void> response = restTemplate.exchange("/api/reference/MPN/EEP", HttpMethod.POST, null, Void.class);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/reference/MPN"));
    }

    @Test
    void createNewReferenceGeneratorForExistingGeneratorThrowsExceptionTest() {
        given(referenceGeneratorService.createReferenceGenerator(anyString(), anyString(), anyLong())).willReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/api/reference/MPN/EEP", HttpMethod.POST, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}