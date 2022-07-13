package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.delivery.DeliveryMapper;
import com.eep.stocker.dto.delivery.GetAllDeliveryResponse;
import com.eep.stocker.dto.delivery.GetDeliveryResponse;
import com.eep.stocker.dto.delivery.UpdateDeliveryRequest;
import com.eep.stocker.services.DeliveryService;
import com.eep.stocker.services.SupplierService;

import static com.eep.stocker.testdata.DeliveryTestData.*;
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
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private SupplierService supplierService;


    @BeforeEach
    void setUp() {

    }

    @Test
    void getDeliveryByIdTest() {
        //arrange
        given(deliveryService.getDeliveryByUid(anyString())).willReturn(Optional.of(delivery1));

        //act
        ResponseEntity<GetDeliveryResponse> response = restTemplate.exchange(
                "/api/delivery/get/" + UUID.randomUUID(),
                HttpMethod.GET,
                null,
                GetDeliveryResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(getDeliveryResponse);
    }

    @Test
    void getAllDeliveriesTest() {
        given(deliveryService.getAllDeliveries()).willReturn(Arrays.asList(delivery1, delivery2));

        ResponseEntity<GetAllDeliveryResponse> response = restTemplate.exchange("/api/delivery/get",
                HttpMethod.GET,
                null,
                GetAllDeliveryResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAllDeliveries()).contains(
                        deliveryMapper.deliveryToGetDeliveryResponse(delivery1),
                        deliveryMapper.deliveryToGetDeliveryResponse(delivery2)
        );
    }

    @Test
    void getDeliveriesBetweenTest() {
        given(deliveryService.getAllDeliveriesBetween(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Arrays.asList(delivery1, delivery2));

        ResponseEntity<GetAllDeliveryResponse> response = restTemplate.exchange("/api/delivery/2017-01-15/2019-09-10",
                HttpMethod.GET,
                null,
                GetAllDeliveryResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAllDeliveries()).contains(
                deliveryMapper.deliveryToGetDeliveryResponse(delivery1),
                deliveryMapper.deliveryToGetDeliveryResponse(delivery2)
        );
    }

    @Test
    void getDeliveriesForSupplierTest() {
        given(supplierService.getSupplierFromId(any(Long.class))).willReturn(Optional.of(supplier));
        given(deliveryService.getAllDeliveriesForSupplier(any(Supplier.class))).willReturn(Arrays.asList(delivery1, delivery2));

        ResponseEntity<GetAllDeliveryResponse> response = restTemplate.exchange("/api/delivery/supplier/1",
                HttpMethod.GET,
                null,
                GetAllDeliveryResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getAllDeliveries()).contains(
                deliveryMapper.deliveryToGetDeliveryResponse(delivery1),
                deliveryMapper.deliveryToGetDeliveryResponse(delivery2)
        );
    }

    @Test
    void createDeliveryTest() {
        Delivery unsavedDelivery = new Delivery();
        unsavedDelivery.setSupplier(supplier);
        unsavedDelivery.setReference("12345");
        unsavedDelivery.setDeliveryDate(LocalDate.now());
        unsavedDelivery.setNote("A note for the delivery");

        Delivery savedDelivery = new Delivery();
        savedDelivery.setSupplier(supplier);
        savedDelivery.setReference("12345");
        savedDelivery.setNote("A note for the delivery");
        savedDelivery.setDeliveryDate(unsavedDelivery.getDeliveryDate());
        savedDelivery.setId(1L);

        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));
        given(deliveryService.saveDelivery(any(Delivery.class))).willReturn(savedDelivery);

        var request = deliveryMapper.map(unsavedDelivery);

        ResponseEntity<GetDeliveryResponse> response = restTemplate.postForEntity("/api/delivery/create",
                request, GetDeliveryResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(deliveryMapper.deliveryToGetDeliveryResponse(savedDelivery));
    }

    @Test
    void updateDeliveryTest() {
        var updateDeliveryRequest = UpdateDeliveryRequest.builder()
                .id(delivery1.getUid().toString())
                .supplierId(supplier.getUid().toString())
                .reference("123456")
                .note("A note for the delivery")
                .build();

        Delivery savedDelivery = new Delivery();
        savedDelivery.setUid(UUID.fromString(updateDeliveryRequest.getId()));
        savedDelivery.setSupplier(supplier);
        savedDelivery.setReference("123456");
        savedDelivery.setNote("A note for the delivery");
        savedDelivery.setId(1L);

        var savedDeliveryDto = deliveryMapper.deliveryToGetDeliveryResponse(savedDelivery);

        given(deliveryService.getDeliveryByUid(delivery1.getUid().toString())).willReturn(Optional.of(savedDelivery));
        given(deliveryService.saveDelivery(any(Delivery.class))).willReturn(savedDelivery);

        ResponseEntity<GetDeliveryResponse> response = restTemplate.exchange("/api/delivery/update",
                HttpMethod.PUT, new HttpEntity<>(updateDeliveryRequest), GetDeliveryResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(savedDeliveryDto);
        assertThat(response.getBody().getReference()).isEqualTo("123456");
    }

    @Test
    void updateNonExistantDeliveryThrowsNonExistantDelivery() {
        var updateDeliveryRequest = UpdateDeliveryRequest.builder()
                .id(delivery1.getUid().toString())
                .supplierId(supplier.getUid().toString())
                .reference("123456")
                .note("A note for the delivery")
                .build();

        given(deliveryService.getDeliveryByUid(anyString())).willReturn(Optional.empty());

        ResponseEntity<Delivery> response = restTemplate.exchange("/api/delivery/update",
                HttpMethod.PUT, new HttpEntity<>(updateDeliveryRequest), Delivery.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteDeliveryTest() {
        given(deliveryService.getDeliveryByUid(anyString())).willReturn(Optional.of(delivery1));

        ResponseEntity<String> response =restTemplate.exchange("/api/delivery/delete/1",
                HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Delivery with ID of " + delivery1.getUid().toString() + " deleted");
    }
}