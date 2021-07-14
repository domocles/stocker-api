package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.SupplierQuoteErrorException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.SupplierService;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseOrderControllerIntegrationTest extends SupplierTestData {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private SupplierService supplierService;

    private PurchaseOrder po1;
    private PurchaseOrder po2;

    @BeforeEach
    void setUp() {


         po1 = new PurchaseOrder();
         po1.setId(1L);
         po1.setSupplier(supplier);
         po1.setPurchaseOrderReference("PO-001");
         po1.setPurchaseOrderDate(new Date());

         po2 = new PurchaseOrder();
         po2.setId(2L);
         po2.setSupplier(supplier);
         po2.setPurchaseOrderReference("PO-002");
         po2.setPurchaseOrderDate(new Date());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void getAllPurchaseOrdersTest() {
        //arrange
        given(purchaseOrderService.getAllPurchaseOrders()).willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<List<PurchaseOrder>> response = restTemplate.exchange("/api/purchase-order/get",
                                                                            HttpMethod.GET,
                                                                            null,
                                                                            new ParameterizedTypeReference<List<PurchaseOrder>>() {});

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @Test
    public void getAllPurchaseOrdersForSupplier() {
        //arrange
        given(supplierService.getSupplierFromId(any(Long.class))).willReturn(Optional.of(supplier));
        given(purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier)).willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<List<PurchaseOrder>> response = restTemplate.exchange("/api/purchase-order/supplier/get/1",
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                new ParameterizedTypeReference<List<PurchaseOrder>>() {});

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);
        assertThat(response.getBody()).contains(po2);
        assertThat(response.getBody()).contains(po1);
    }

    @Test
    public void  getAllPurchaseOrdersForSupplierWhoDoesntExist() {
        //arrange
        given(supplierService.getSupplierFromId(any(Long.class))).willReturn(Optional.empty());
        given(purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier)).willThrow(SupplierDoesNotExistException.class);

        //act
        ResponseEntity<Void> response = restTemplate.exchange("/api/purchase-order/supplier/get/1",
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllPurchaseOrdersBetweenDates() {
        //arrange
        given(purchaseOrderService.getAllPurchaseOrdersBetween(any(Date.class), any(Date.class)))
                .willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<List<PurchaseOrder>> response = restTemplate.exchange("/api/purchase-order/2017-01-15/2019-09-10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PurchaseOrder>>() {});

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(po1);
        assertThat(response.getBody()).contains(po2);
    }

    @Test
    public void createPurchaseOrderTest() {
        //arrange
        PurchaseOrder po3unsaved = new PurchaseOrder();
        po3unsaved.setSupplier(supplier);
        po3unsaved.setPurchaseOrderReference("PO-003");
        po3unsaved.setPurchaseOrderDate(new Date());

        PurchaseOrder po3saved = new PurchaseOrder();
        po3saved.setId(3L);
        po3saved.setSupplier(supplier);
        po3saved.setPurchaseOrderReference("PO-003");
        po3saved.setPurchaseOrderDate(new Date());

        given(purchaseOrderService.savePurchaseOrder(po3unsaved)).willReturn(po3saved);

        //act
        ResponseEntity<PurchaseOrder> response = restTemplate.postForEntity("/api/purchase-order/create", po3unsaved, PurchaseOrder.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(po3saved);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void updatePurchaseOrderTest() {
        //arrange
        PurchaseOrder po3unsaved = new PurchaseOrder();
        po3unsaved.setId(3L);
        po3unsaved.setSupplier(supplier);
        po3unsaved.setPurchaseOrderReference("PO-003");
        po3unsaved.setPurchaseOrderDate(new Date());

        PurchaseOrder po3saved = new PurchaseOrder();
        po3saved.setId(3L);
        po3saved.setSupplier(supplier);
        po3saved.setPurchaseOrderReference("PO-004");
        po3saved.setPurchaseOrderDate(new Date());

        given(purchaseOrderService.savePurchaseOrder(any(PurchaseOrder.class))).willReturn(po3saved);


        //act
        ResponseEntity<PurchaseOrder> response = restTemplate.exchange("/api/purchase-order/update",
                    HttpMethod.PUT,
                    new HttpEntity<>(po3unsaved),
                    PurchaseOrder.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPurchaseOrderReference()).isEqualTo("PO-004");
    }

    @Test
    public void purchaseOrderDeleteTest() {
        given(purchaseOrderService.getPurchaseOrderFromId(any(Long.class))).willReturn(Optional.of(po1));

        ResponseEntity<String> response = restTemplate.exchange("/api/purchase-order/delete/1",
                HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}