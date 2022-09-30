package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.*;
import com.eep.stocker.dto.deliveryline.*;
import com.eep.stocker.services.*;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryLineControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DeliveryLineMapper deliveryLineMapper;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private DeliveryLineService deliveryLineService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private PurchaseOrderLineService purchaseOrderLineService;

    @MockBean
    private StockableProductService stockableProductService;

    @MockBean
    private StockTransactionService stockTransactionService;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;
    private StockableProduct mf220;
    private StockableProduct MF286;

    Supplier ukf;
    Delivery delivery1;
    Delivery delivery2;
    Delivery delivery3;

    private DeliveryLine deliveryLine1;
    private DeliveryLine deliveryLine2;
    private DeliveryLine deliveryLine3;

    @BeforeEach
    void setUp() {

        po1 = new PurchaseOrder();
        po1.setId(1L);
        po1.setSupplier(shelleys);
        po1.setPurchaseOrderReference("PO-001");
        po1.setPurchaseOrderDate(LocalDate.now());

        po2 = new PurchaseOrder();
        po2.setId(2L);
        po2.setSupplier(shelleys);
        po2.setPurchaseOrderReference("PO-002");
        po2.setPurchaseOrderDate(LocalDate.now());

        mf220 = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        MF286 = StockableProduct.builder()
                .id(2L)
                .name("MF286")
                .mpn("EEP200919002")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.45)
                .inStock(75.0)
                .build();

        unsavedPoLine1 = new PurchaseOrderLine();
        unsavedPoLine1.setNote("First purchase order line");
        unsavedPoLine1.setPurchaseOrder(po1);
        unsavedPoLine1.setStockableProduct(mf220);
        unsavedPoLine1.setQty(25.0D);
        unsavedPoLine1.setPrice(1.27D);

        poLine1 = new PurchaseOrderLine();
        poLine1.setId(1L);
        poLine1.setNote("First purchase order line");
        poLine1.setPurchaseOrder(po1);
        poLine1.setStockableProduct(mf220);
        poLine1.setQty(25.0D);
        poLine1.setPrice(1.27D);

        poLine2 = new PurchaseOrderLine();
        poLine2.setId(2L);
        poLine2.setNote("Second purchase order line");
        poLine2.setPurchaseOrder(po1);
        poLine2.setStockableProduct(MF286);
        poLine2.setQty(100.0D);
        poLine2.setPrice(1.35D);

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

        deliveryLine1 = new DeliveryLine();
        deliveryLine1.setId(1L);
        deliveryLine1.setDelivery(delivery1);
        deliveryLine1.setPurchaseOrderLine(poLine1);
        deliveryLine1.setNote("A note");
        deliveryLine1.setQuantityDelivered(15.0D);
        StockTransaction transaction1 = new StockTransaction(poLine1.getStockableProduct(),
                deliveryLine1.getQuantityDelivered(), "Delivery No. " + delivery1.getReference(),
                "Test stock transaction");
        transaction1.setId(1L);
        deliveryLine1.setStockTransaction(transaction1);

        deliveryLine2 = new DeliveryLine();
        deliveryLine2.setId(2L);
        deliveryLine2.setDelivery(delivery2);
        deliveryLine2.setPurchaseOrderLine(poLine1);
        deliveryLine2.setNote("A note");
        deliveryLine2.setQuantityDelivered(10.0D);
        StockTransaction transaction2 = new StockTransaction(poLine1.getStockableProduct(),
                deliveryLine2.getQuantityDelivered(), "Delivery No. " + delivery2.getReference(),
                "Test stock transaction");
        transaction2.setId(2L);
        deliveryLine2.setStockTransaction(transaction2);

        deliveryLine3 = new DeliveryLine();
        deliveryLine3.setId(3L);
        deliveryLine3.setDelivery(delivery1);
        deliveryLine3.setPurchaseOrderLine(poLine2);
        deliveryLine3.setNote("A note");
        deliveryLine3.setQuantityDelivered(100.0D);
        StockTransaction transaction3 = new StockTransaction(poLine2.getStockableProduct(),
                deliveryLine3.getQuantityDelivered(), "Delivery No. " + delivery3.getReference(),
                "Test stock transaction");
        transaction3.setId(3L);
        deliveryLine3.setStockTransaction(transaction3);
    }

    @Test
    void getDeliveryLineByUidTest() {
        //arrange
        given(deliveryLineService.getDeliveryLineByUid(anyString())).willReturn(Optional.of(deliveryLine2));

        //act
        var response = restTemplate.exchange("/api/delivery-line/" + deliveryLine2.getUid().toString(),
                HttpMethod.GET,
                null,
                GetDeliveryLineResponse.class);

        var testResponse = deliveryLineMapper.mapToGetResponse(deliveryLine2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void getDeliveryLineByInvalidUidTest() {
        //act
        var response = restTemplate.exchange("/api/delivery-line/invalid",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getDetails()).contains("getDeliveryLineById.uid: Delivery Line Id must be a UUID")
        );
    }

    @Test
    void getAllDeliveryLines() {
        //arrange
        given(deliveryLineService.getAllDeliveryLines()).willReturn(Arrays.asList(deliveryLine1, deliveryLine2, deliveryLine3));

        //act
        var response = restTemplate.exchange("/api/delivery-line/",
                HttpMethod.GET,
                null,
                GetAllDeliveryLinesResponse.class
        );

        var testResponse1 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine1);
        var testResponse2 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine2);
        var testResponse3 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine3);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().deliveryLines).contains(testResponse1, testResponse2, testResponse3)
        );
    }

    @Test
    void getAllDeliveryLinesForSupplierTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(shelleys));
        given(deliveryLineService.getAllDeliveryLinesForSupplier(any(Supplier.class)))
                .willReturn(Arrays.asList(deliveryLine2, deliveryLine1));

        var response = restTemplate.exchange("/api/delivery-line/supplier/" + shelleys.getUid().toString(),
                HttpMethod.GET,
                null,
                GetDeliveryLinesBySupplierResponse.class
        );

        var testDeliveryLine1 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine1);
        var testDeliveryLine2 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().deliveryLines).contains(testDeliveryLine1, testDeliveryLine2)
        );
    }

    @Test
    void getAllDeliveryLinesForProductTest() {
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(mf220));
        given(deliveryLineService.getAllDeliveryLinesForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(deliveryLine1, deliveryLine3));

        var response = restTemplate.exchange(
                "/api/delivery-line/stockable-product/" + mf220.getUid().toString(),
                HttpMethod.GET,
                null,
                GetDeliveryLinesByProductResponse.class);

        var testResult1 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine1);
        var testResult2 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).deliveryLines).contains(testResult1, testResult2)
        );
    }

    @Test
    void getAllDeliveryLinesForDeliveryTest() {
        given(deliveryService.getDeliveryByUid(anyString())).willReturn(Optional.of(delivery1));
        given(deliveryLineService.getAllDeliveryLinesForDelivery(any(UUID.class)))
                .willReturn(Arrays.asList(deliveryLine1, deliveryLine3));

        var response = restTemplate.exchange(
                "/api/delivery-line/delivery/" + mf220.getUid().toString(),
                HttpMethod.GET,
                null,
                GetAllByDeliveryResponse.class);

        var testResult1 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine1);
        var testResult2 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).deliveryLines).contains(testResult1, testResult2)
        );
    }

    @Test
    void getAllDeliveryLinesForPurchaseOrderTest() {
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po1));
        given(deliveryLineService.getAllDeliveryLinesForPurchaseOrder(any(PurchaseOrder.class)))
                .willReturn(Arrays.asList(deliveryLine3, deliveryLine1));

        var response = restTemplate.exchange(
                "/api/delivery-line/purchase-order/" + po1.getUid().toString(),
                HttpMethod.GET,
                null,
                GetDeliveryLinesByPurchaseOrderResponse.class);

        var testResult1 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine1);
        var testResult2 = deliveryLineMapper.mapToLowDetailResponse(deliveryLine3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).deliveryLines).contains(testResult1, testResult2)
        );
    }

    @Test
    void deleteDeliveryLineTest() {
        given(deliveryLineService.getDeliveryLineByUid(anyString())).willReturn(Optional.of(deliveryLine1));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/delivery-line/" + deliveryLine1.getUid().toString(),
                HttpMethod.DELETE,
                null,
                String.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo("Delivery Line with ID of " + deliveryLine1.getUid().toString() + " deleted")
        );
    }

    @Test
    void createDeliveryLineTest() {
        DeliveryLine unsavedDeliveryLine = new DeliveryLine();
        unsavedDeliveryLine.setPurchaseOrderLine(poLine1);
        unsavedDeliveryLine.setNote("A note");
        unsavedDeliveryLine.setQuantityDelivered(25.0D);
        unsavedDeliveryLine.setDelivery(delivery2);
        StockTransaction transaction = new StockTransaction(poLine1.getStockableProduct(),
                unsavedDeliveryLine.getQuantityDelivered(), "Delivery No. " + delivery2.getReference(),
                "Test stock transaction");
        unsavedDeliveryLine.setStockTransaction(transaction);

        DeliveryLine savedDeliveryLine = new DeliveryLine();
        savedDeliveryLine.setId(1L);
        savedDeliveryLine.setPurchaseOrderLine(poLine1);
        savedDeliveryLine.setNote("A note");
        savedDeliveryLine.setQuantityDelivered(25.0D);
        savedDeliveryLine.setDelivery(delivery2);
        StockTransaction stockTransaction = new StockTransaction(poLine1.getStockableProduct(),
                unsavedDeliveryLine.getQuantityDelivered(), "Delivery No. " + delivery2.getReference(),
                "Test stock transaction");
        savedDeliveryLine.setStockTransaction(stockTransaction);

        var request = new CreateDeliveryLineRequest();
        request.setPurchaseOrderLineId(poLine1.getUid().toString());
        request.setDeliveryId(delivery2.getUid().toString());
        request.setStockTransactionId(transaction.getUid().toString());
        request.setQuantityDelivered(25.0D);
        request.setNote("A note");

        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString())).willReturn(Optional.of(poLine1));
        given(deliveryService.getDeliveryByUid(anyString())).willReturn(Optional.of(delivery2));
        given(stockTransactionService.getStockTransactionByUid(anyString())).willReturn(Optional.of(transaction));
        given(deliveryLineService.save(unsavedDeliveryLine)).willReturn(savedDeliveryLine);

        var response = restTemplate.postForEntity("/api/delivery-line/",
                request,
                CreateDeliveryLineResponse.class);

        var testResponse = deliveryLineMapper.mapToCreateResponse(savedDeliveryLine);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void updateDeliveryLineTest() {
        var updatedDeliveryLine = deliveryLine1.toBuilder().quantityDelivered(73.0D).build();
        given(deliveryLineService.getDeliveryLineByUid(anyString())).willReturn(Optional.of(deliveryLine1));
        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString()))
                .willReturn(Optional.of(deliveryLine1.getPurchaseOrderLine()));
        given(deliveryService.getDeliveryByUid(anyString())).willReturn(Optional.of(deliveryLine1.getDelivery()));
        given(stockTransactionService.getStockTransactionByUid(anyString())).willReturn(Optional.of(deliveryLine1.getStockTransaction()));
        given(deliveryLineService.save(any(DeliveryLine.class))).willReturn(updatedDeliveryLine);

        var request = new UpdateDeliveryLineRequest();
        request.setPurchaseOrderLineId(deliveryLine1.getPurchaseOrderLine().getUid().toString());
        request.setDeliveryId(deliveryLine1.getDelivery().getUid().toString());
        request.setStockTransactionId(deliveryLine1.getStockTransaction().getUid().toString());
        request.setQuantityDelivered(73.0D);
        request.setNote(deliveryLine1.getNote());

        var response = restTemplate.exchange(
                "/api/delivery-line/" + deliveryLine1.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateDeliveryLineResponse.class
        );

        var testResponse = deliveryLineMapper.mapToUpdateResponse(updatedDeliveryLine);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }
}