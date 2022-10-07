package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.*;
import com.eep.stocker.dto.purchaseorderline.*;
import com.eep.stocker.services.*;
import com.eep.stocker.testdata.SupplierTestData;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseOrderLineControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PurchaseOrderLineMapper mapper;

    @MockBean
    private PurchaseOrderLineService purchaseOrderLineService;

    @MockBean
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private StockableProductService stockableProductService;

    @MockBean
    private DeliveryLineService deliveryLineService;

    @MockBean
    private SupplierService supplierService;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;
    private StockableProduct mf220;
    private StockableProduct MF286;

    @BeforeEach
    public void setup() {
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

        poLine1 = new PurchaseOrderLine();
        poLine1.setId(1L);
        poLine1.setNote("First purchase order line");
        poLine1.setPurchaseOrder(po1);
        poLine1.setStockableProduct(mf220);
        poLine1.setQty(25.0D);
        poLine1.setPrice(1.27D);

        unsavedPoLine1 = new PurchaseOrderLine();
        unsavedPoLine1.setNote("First purchase order line");
        unsavedPoLine1.setPurchaseOrder(po1);
        unsavedPoLine1.setStockableProduct(mf220);
        unsavedPoLine1.setQty(25.0D);
        unsavedPoLine1.setPrice(1.27D);

        poLine2 = new PurchaseOrderLine();
        poLine2.setId(2L);
        poLine2.setNote("Second purchase order line");
        poLine2.setPurchaseOrder(po1);
        poLine2.setStockableProduct(MF286);
        poLine2.setQty(100.0D);
        poLine2.setPrice(1.35D);
    }

    @Test
    void getPurchaseOrderLineByUidTest() {
        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString())).willReturn(Optional.of(poLine1));

        ResponseEntity<GetPurchaseOrderLineResponse> response = restTemplate.exchange(
                "/api/purchase-order-line/" + poLine1.getUid().toString(),
                HttpMethod.GET,
                null,
                GetPurchaseOrderLineResponse.class
        );

        var res = mapper.mapToGetResponse(poLine1, 0.0);

        assertAll(
                () -> assertThat(response.getBody()).isEqualTo(res)
        );
    }

    @Test
    void getAllPurchaseOrderLinesTest() {
        //amend
        given(purchaseOrderLineService.getAllPurchaseOrderLines()).willReturn(Arrays.asList(poLine1, poLine2));


        //act
        ResponseEntity<GetAllPurchaseOrderLinesResponse> response = restTemplate.exchange("/api/purchase-order-line/",
                HttpMethod.GET,
                null,
                GetAllPurchaseOrderLinesResponse.class);

        var testLine1 = mapper.mapToGetLowDetailResponse(poLine1);
        var testLine2 = mapper.mapToGetLowDetailResponse(poLine2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrderLines()).contains(testLine1, testLine2)
        );
    }

    @Test
    void getAllPurchaseOrderLinesForProductTest() {
        //ammend
        Optional<StockableProduct> product = Optional.of(MF286);
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(product);
        List<PurchaseOrderLine> lines = Arrays.asList(poLine1, poLine2);
        given(purchaseOrderLineService.getAllPurchaseOrderLinesForProduct(any(StockableProduct.class)))
                .willReturn(lines);

        //act
        ResponseEntity<GetPurchaseOrderLinesByProductResponse> response = restTemplate.exchange("/api/purchase-order-line/product/" + MF286.getUid() +"/",
                HttpMethod.GET,
                null,
                GetPurchaseOrderLinesByProductResponse.class);

        var testOrderLine1 = mapper.mapToGetLowDetailResponse(poLine1);
        var testOrderLine2 = mapper.mapToGetLowDetailResponse(poLine2);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrderLines()).contains(testOrderLine1, testOrderLine2)
        );
    }

    @Test
    void getAllPurchaseOrderLinesForNonexistantProductThrowsNonexistantProductExceptionTest() {
        //ammend
        given(stockableProductService.getStockableProductByID(any(Long.class))).willReturn(Optional.empty());

        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order-line/product/" + MF286.getUid() +"/",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getDetails().get(0)).isEqualTo(String.format("Stockable product with id of %s, does not exist", MF286.getUid()))
        );
    }

    @Test
    void getAllPurchaseOrderLinesForPurchaseOrderTest() {
        //ammend
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po1));
        given(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(any(PurchaseOrder.class)))
                .willReturn(Arrays.asList(poLine1, poLine2));

        //act
        ResponseEntity<GetPurchaseOrderLinesByPurchaseOrderResponse> response = restTemplate.exchange("/api/purchase-order-line/purchase-order/" + po1.getUid() + "/",
                HttpMethod.GET,
                null,
                GetPurchaseOrderLinesByPurchaseOrderResponse.class);

        var testLine = mapper.mapToGetLowDetailResponse(poLine1);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAllPurchaseOrderLines()).contains(testLine)
        );
    }

    @Test
    void getAllPurchaseOrderLinesForPurchaseOrderThrowsNonExistantPurchaseOrderTest() {
        //ammend
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.empty());

        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order-line/purchase-order/"+po1.getUid(),
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBalanceOfPurchaseOrderLineTest() {
        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString())).willReturn(Optional.of(poLine1));
        given(deliveryLineService.getSumDeliveredForOrderLine(any(PurchaseOrderLine.class))).willReturn(Optional.of(10D));

        ResponseEntity<Double> response = restTemplate.exchange(
                "/api/purchase-order-line/balance/" + poLine1.getUid().toString(),
                HttpMethod.GET,
                null,
                Double.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(poLine1.getQty() - 10D)
        );
    }

    @Test
    void getAllPurchaseOrderLinesForSupplierTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(shelleys));
        given(purchaseOrderService.getAllPurchaseOrdersForSupplier(any(Supplier.class))).willReturn(List.of(po1, po2));
        given(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(po1)).willReturn(List.of(poLine1));
        given(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(po2)).willReturn(List.of(poLine2));

        var response = restTemplate.exchange("/api/purchase-order-line/supplier/" + shelleys.getUid() +"/",
                HttpMethod.GET,
                null,
                GetPurchaseOrderLinesBySupplierResponse.class);

        var testResponse1 = mapper.mapToGetLowDetailResponse(poLine1);
        var testResponse2 = mapper.mapToGetLowDetailResponse(poLine2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).allPurchaseOrderLines).contains(testResponse1, testResponse2)
        );
    }

    @Test
    void savePurchaseOrderLineTest() {
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po1));
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF286));
        given(purchaseOrderLineService.savePurchaseOrderLine(any(PurchaseOrderLine.class))).willReturn(poLine1);

        var request = new CreatePurchaseOrderLineRequest();
        request.setPurchaseOrderId(poLine1.getUid().toString());
        request.setStockableProductId(poLine1.getStockableProduct().getUid().toString());
        request.setQty(poLine1.getQty());
        request.setNote(poLine1.getNote());

        ResponseEntity<CreatePurchaseOrderLineResponse> response = restTemplate.postForEntity("/api/purchase-order-line/",
                request, CreatePurchaseOrderLineResponse.class);

        var testResponse = mapper.mapToCreateResponse(poLine1);

        assertAll(
                ()-> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void updateStatusOfPurchaseOrderLineTest() {
        var updatedOrderLine = poLine1.toBuilder().status(Status.CANCELLED).build();
        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString())).willReturn(Optional.of(poLine1));
        given(purchaseOrderLineService.savePurchaseOrderLine(any(PurchaseOrderLine.class))).willReturn(updatedOrderLine);

        var request = new UpdateStatusRequest(Status.CANCELLED);

        var response = restTemplate.exchange(
                "/api/purchase-order-line/status/" + poLine1.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateStatusResponse.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getStatus()).isEqualTo(Status.CANCELLED)
        );
    }

    @Test
    void updatePurchaseOrderLineTest() {
        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString())).willReturn(Optional.of(poLine2));
        given(purchaseOrderService.getPurchaseOrderFromUid(any(UUID.class))).willReturn(Optional.of(po1));
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF286));
        given(purchaseOrderLineService.savePurchaseOrderLine(any(PurchaseOrderLine.class))).willReturn(poLine1);

        var request = new UpdatePurchaseOrderLineRequest();
        request.setNote(poLine1.getNote());
        request.setPrice(poLine1.getPrice());
        request.setQty(poLine1.getQty());
        request.setPurchaseOrderId(poLine1.getPurchaseOrder().getUid().toString());
        request.setStockableProductId(poLine1.getStockableProduct().getUid().toString());

        var response = restTemplate.exchange("/api/purchase-order-line/" + poLine1.getUid().toString(),
                HttpMethod.PUT, new HttpEntity<>(request), UpdatePurchaseOrderLineResponse.class);

        var testResponse = mapper.mapToUpdateResponse(poLine1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void deletePurchaseOrderLine() {
        given(purchaseOrderLineService.getPurchaseOrderLineByUid(anyString())).willReturn(Optional.of(poLine1));

        ResponseEntity<String> response = restTemplate.exchange("/api/purchase-order-line/" + poLine1.getUid(),
                HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(String.format("Purchase Order Line with ID %s has been deleted", poLine1.getUid()));
    }
}
