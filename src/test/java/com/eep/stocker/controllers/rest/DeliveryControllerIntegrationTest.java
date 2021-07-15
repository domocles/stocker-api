package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.DeliveryService;
import com.eep.stocker.services.SupplierService;

import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private SupplierService supplierService;

    private Delivery delivery1;
    private Delivery delivery2;

    @BeforeEach
    void setUp() {
        
        delivery1 = new Delivery();
        delivery1.setSupplier(supplier);
        delivery1.setReference("12345");
        delivery1.setNote("Last minute delivery");

        delivery2 = new Delivery();
        delivery2.setSupplier(supplier);
        delivery2.setReference("12345");
        delivery2.setNote("Last minute delivery 2");
    }

    @Test
    void getDeliveryByIdTest() {
        //arrange
        given(deliveryService.getDeliveryById(anyLong())).willReturn(Optional.of(delivery1));

        //act
        ResponseEntity<Delivery> response = restTemplate.exchange("/api/delivery/get/1",
                HttpMethod.GET,
                null,
                Delivery.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(delivery1);
    }

    @Test
    void getAllDeliveriesTest() {
        given(deliveryService.getAllDeliveries()).willReturn(Arrays.asList(delivery1, delivery2));

        ResponseEntity<List<Delivery>> response = restTemplate.exchange("/api/delivery/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Delivery>>() { });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains(delivery1, delivery2);
    }

    @Test
    void getDeliveriesBetweenTest() {
        given(deliveryService.getAllDeliveriesBetween(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Arrays.asList(delivery1, delivery2));

        ResponseEntity<List<Delivery>> response = restTemplate.exchange("/api/delivery/2017-01-15/2019-09-10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Delivery>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(delivery1, delivery2);
    }

    @Test
    void getDeliveriesForSupplierTest() {
        given(supplierService.getSupplierFromId(any(Long.class))).willReturn(Optional.of(supplier));
        given(deliveryService.getAllDeliveriesForSupplier(any(Supplier.class))).willReturn(Arrays.asList(delivery1, delivery2));

        ResponseEntity<List<Delivery>> response = restTemplate.exchange("/api/delivery/supplier/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Delivery>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(delivery1, delivery2);
    }

    @Test
    void createDeliveryTest() {
        Delivery unsavedDelivery = new Delivery();
        unsavedDelivery.setSupplier(supplier);
        unsavedDelivery.setReference("12345");
        unsavedDelivery.setNote("A note for the delivery");

        Delivery savedDelivery = new Delivery();
        savedDelivery.setSupplier(supplier);
        savedDelivery.setReference("12345");
        savedDelivery.setNote("A note for the delivery");
        savedDelivery.setId(1L);

        given(deliveryService.saveDelivery(unsavedDelivery)).willReturn(savedDelivery);

        ResponseEntity<Delivery> response = restTemplate.postForEntity("/api/delivery/create",
                unsavedDelivery, Delivery.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(savedDelivery);
    }

    @Test
    void updateDeliveryTest() {
        Delivery unsavedDelivery = new Delivery();
        unsavedDelivery.setSupplier(supplier);
        unsavedDelivery.setReference("12345");
        unsavedDelivery.setNote("A note for the delivery");

        Delivery savedDelivery = new Delivery();
        savedDelivery.setSupplier(supplier);
        savedDelivery.setReference("123456");
        savedDelivery.setNote("A note for the delivery");
        savedDelivery.setId(1L);

        given(deliveryService.saveDelivery(unsavedDelivery)).willReturn(savedDelivery);

        ResponseEntity<Delivery> response = restTemplate.exchange("/api/delivery/update",
                HttpMethod.PUT, new HttpEntity<>(unsavedDelivery), Delivery.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(savedDelivery);
        assertThat(response.getBody().getReference()).isEqualTo("123456");
    }

    @Test
    public void deleteDeliveryTest() {
        given(deliveryService.getDeliveryById(anyLong())).willReturn(Optional.of(delivery1));

        ResponseEntity<String> response =restTemplate.exchange("/api/delivery/delete/1",
                HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}