package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Status;
import com.eep.stocker.dto.purchaseorder.*;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.SupplierService;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseOrderControllerIntegrationTest extends SupplierTestData {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PurchaseOrderMapper mapper;

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
         po1.setPurchaseOrderDate(LocalDate.now());

         po2 = new PurchaseOrder();
         po2.setId(2L);
         po2.setSupplier(supplier);
         po2.setPurchaseOrderReference("PO-002");
         po2.setPurchaseOrderDate(LocalDate.now());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPurchaseOrderTestThrowsExceptionInvalidId() {
        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order/sam123",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertNotNull(response.getBody())
        );
    }

    @Test
    void getPurchaseOrderTest() {
        //arrange
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po1));

        //act
        ResponseEntity<GetPurchaseOrderResponse> response = restTemplate.exchange("/api/purchase-order/92f129c2-70eb-4f45-83bb-23b1b48be413",
                HttpMethod.GET,
                null,
                GetPurchaseOrderResponse.class);

        //assert
        var purchaseOrder = mapper.mapGetResponse(po1);
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(purchaseOrder, response.getBody()),
                () -> assertThat(purchaseOrder.getSupplier()).isNotNull(),
                () -> assertThat(purchaseOrder.getSupplier()).isEqualTo(getSupplierResponse)
        );
    }

    @Test
    void getAllPurchaseOrdersTest() {
        //arrange
        given(purchaseOrderService.getAllPurchaseOrders()).willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<GetAllPurchaseOrdersResponse> response = restTemplate.exchange("/api/purchase-order/",
                                                                            HttpMethod.GET,
                                                                            null,
                                                                            GetAllPurchaseOrdersResponse.class);

        var res1 = mapper.mapGetResponse(po1);
        var res2 = mapper.mapGetResponse(po2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).allPurchaseOrders.size()).isPositive(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrders()).contains(res1, res2)
        );
    }

    @Test
    void getAllPurchaseOrdersForInvalidSupplierUidSendsErrorResponse() {
        //arrange
        given(supplierService.getSupplierFromId(any(Long.class))).willReturn(Optional.of(supplier));
        given(purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier)).willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order/supplier/1/",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody()).isNotNull()
        );
    }

    @Test
    void getAllPurchaseOrdersForSupplier() {
        //arrange
        given(supplierService.getSupplierFromUid(any(String.class))).willReturn(Optional.of(supplier));
        given(purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier)).willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<GetAllPurchaseOrdersResponse> response = restTemplate.exchange("/api/purchase-order/supplier/" + supplier.getUid().toString() +"/",
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                GetAllPurchaseOrdersResponse.class);

        var res1 = mapper.mapGetResponse(po1);
        var res2 = mapper.mapGetResponse(po2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrders().size()).isPositive(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrders()).contains(res1, res2)
        );
    }

    @Test
    void getAllPurchaseOrdersForReferenceTest() {
        //arrange
        given(purchaseOrderService.getAllPurchaseOrdersBySupplierReference(anyString())).willReturn(Arrays.asList(po1, po2));

        //act
        var response = restTemplate.exchange("/api/purchase-order/supplier-reference/PO001/",
                HttpMethod.GET,
                null,
                GetAllPurchaseOrdersResponse.class);

        var res1 = mapper.mapGetResponse(po1);
        var res2 = mapper.mapGetResponse(po2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrders().size()).isPositive(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrders()).contains(res1, res2)
        );
    }

    @Test
    void  getAllPurchaseOrdersForSupplierWhoDoesntExist() {
        //arrange
        given(supplierService.getSupplierFromId(any(Long.class))).willReturn(Optional.empty());
        given(purchaseOrderService.getAllPurchaseOrdersForSupplier(supplier)).willThrow(SupplierDoesNotExistException.class);

        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order/supplier/get/" + supplier.getUid().toString(),
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAllPurchaseOrdersBetweenDates() {
        //arrange
        given(purchaseOrderService.getAllPurchaseOrdersBetween(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Arrays.asList(po1, po2));

        //act
        ResponseEntity<GetAllPurchaseOrdersResponse> response = restTemplate.exchange("/api/purchase-order/2017-01-15/2019-09-10/",
                HttpMethod.GET,
                null,
                GetAllPurchaseOrdersResponse.class);

        var res1 = mapper.mapGetResponse(po1);
        var res2 = mapper.mapGetResponse(po2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrders()).contains(res1, res2)
        );
    }

    @Test
    void createPurchaseOrderTest() {
        //arrange
        PurchaseOrder po3unsaved = new PurchaseOrder();
        po3unsaved.setSupplier(supplier);
        po3unsaved.setPurchaseOrderReference("PO-003");
        po3unsaved.setPurchaseOrderDate(LocalDate.now());

        var request = new CreatePurchaseOrderRequest();
        request.setPurchaseOrderReference(po3unsaved.getPurchaseOrderReference());
        request.setSupplierId(supplier.getUid().toString());

        PurchaseOrder po3saved = new PurchaseOrder();
        po3saved.setId(3L);
        po3saved.setSupplier(supplier);
        po3saved.setPurchaseOrderReference("PO-003");
        po3saved.setPurchaseOrderDate(LocalDate.now());

        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));
        given(purchaseOrderService.savePurchaseOrder(any(PurchaseOrder.class))).willReturn(po3saved);

        //act
        ResponseEntity<CreatePurchaseOrderResponse> response = restTemplate.postForEntity(
                "/api/purchase-order/",
                request,
                CreatePurchaseOrderResponse.class);

        var testResponse = mapper.mapCreateResponse(po3saved);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull()
        );
    }

    @Test
    void updatePurchaseOrderTest() {
        //arrange
        PurchaseOrder po3unsaved = new PurchaseOrder();
        po3unsaved.setId(3L);
        po3unsaved.setSupplier(supplier);
        po3unsaved.setPurchaseOrderReference("PO-003");
        po3unsaved.setPurchaseOrderDate(LocalDate.now());

        var request = UpdatePurchaseOrderRequest.builder().purchaseOrderReference("PO-004")
                .supplierId(supplier.getUid().toString())
                .status(Status.CLOSED)
                .build();

        PurchaseOrder po3saved = new PurchaseOrder();
        po3saved.setId(3L);
        po3saved.setSupplier(supplier);
        po3saved.setPurchaseOrderReference("PO-004");
        po3saved.setPurchaseOrderDate(LocalDate.now());

        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po3unsaved));
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));
        given(purchaseOrderService.savePurchaseOrder(any(PurchaseOrder.class))).willReturn(po3saved);

        //act
        ResponseEntity<UpdatePurchaseOrderResponse> response = restTemplate.exchange("/api/purchase-order/" + po3unsaved.getUid().toString(),
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    UpdatePurchaseOrderResponse.class);

        var testResponse = mapper.mapToUpdateResponse(po3saved);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );

    }

    @Test
    public void purchaseOrderDeleteTest() {
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po1));

        ResponseEntity<String> response = restTemplate.exchange("/api/purchase-order/" + po1.getUid().toString(),
                HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void purchaseOrderDeleteWithInvalidIdTest() {

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order/1",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}