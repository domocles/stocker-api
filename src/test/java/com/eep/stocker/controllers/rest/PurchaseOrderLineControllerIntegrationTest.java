package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.PurchaseOrderLineService;
import com.eep.stocker.services.PurchaseOrderService;
import com.eep.stocker.services.StockableProductService;
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


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PurchaseOrderLineControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PurchaseOrderLineService purchaseOrderLineService;

    @MockBean
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private StockableProductService stockableProductService;

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
        po1.setPurchaseOrderDate(new Date());

        po2 = new PurchaseOrder();
        po2.setId(2L);
        po2.setSupplier(supplier);
        po2.setPurchaseOrderReference("PO-002");
        po2.setPurchaseOrderDate(new Date());

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
    public void getAllPurchaseOrderLinesTest() {
        //ammend
        given(purchaseOrderLineService.getAllPurchaseOrderLines()).willReturn(Arrays.asList(poLine1, poLine2));


        //act
        ResponseEntity<List<PurchaseOrderLine>> response = restTemplate.exchange("/api/purchase-order-line/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PurchaseOrderLine>>(){});

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains(poLine1);
        assertThat(response.getBody()).contains(poLine2);
    }

    @Test
    public void getAllPurchaseOrderLinesForProductTest() {
        //ammend
        Optional<StockableProduct> product = Optional.of(MF286);
        given(stockableProductService.getStockableProductByID(any(Long.class))).willReturn(product);
        List<PurchaseOrderLine> lines = Arrays.asList(poLine1, poLine2);
        given(purchaseOrderLineService.getAllPurchaseOrderLinesForProduct(any(StockableProduct.class)))
                .willReturn(lines);

        //act
        ResponseEntity<List<PurchaseOrderLine>> response = restTemplate.exchange("/api/purchase-order-line/get/product/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PurchaseOrderLine>>() { });

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(poLine1);
    }

    @Test
    public void getAllPurchaseOrderLinesForNonexistantProductThrowsNonexistantProductExceptionTest() {
        //ammend
        given(stockableProductService.getStockableProductByID(any(Long.class))).willReturn(Optional.empty());

        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order-line/get/product/1",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails().get(0)).isEqualTo("Stockable product with ID of 1 does not exist");
    }

    @Test
    public void getAllPurchaseOrderLinesForPurchaseOrderTest() {
        //ammend
        given(purchaseOrderService.getPurchaseOrderFromId(any(Long.class))).willReturn(Optional.of(po1));
        given(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(any(PurchaseOrder.class)))
                .willReturn(Arrays.asList(poLine1, poLine2));

        //act
        ResponseEntity<List<PurchaseOrderLine>> response = restTemplate.exchange("/api/purchase-order-line/get/purchase-order/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PurchaseOrderLine>>() { });

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(poLine1);
    }

    @Test
    public void getAllPurchaseOrderLinesForPurchaseOrderThrowsNonExistantPurchaseOrderTest() {
        //ammend
        given(purchaseOrderService.getPurchaseOrderFromId(any(Long.class))).willReturn(Optional.empty());

        //act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/purchase-order-line/get/purchase-order/1",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void savePurchaseOrderLineTest() {
        given(purchaseOrderLineService.savePurchaseOrderLine(any(PurchaseOrderLine.class))).willReturn(poLine1);

        ResponseEntity<PurchaseOrderLine> response = restTemplate.postForEntity("/api/purchase-order-line/save",
                unsavedPoLine1, PurchaseOrderLine.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody()).isEqualTo(poLine1);
    }

    @Test
    public void updatePurchaseOrderLineTest() {
        given(purchaseOrderLineService.savePurchaseOrderLine(any(PurchaseOrderLine.class))).willReturn(poLine1);

        ResponseEntity<PurchaseOrderLine> response = restTemplate.exchange("/api/purchase-order-line/update",
                HttpMethod.PUT, new HttpEntity<>(poLine2), PurchaseOrderLine.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(poLine1);
    }

    @Test
    public void deletePurchaseOrderLine() {
        given(purchaseOrderLineService.getPurchaseOrderLineById(any(Long.class))).willReturn(Optional.of(poLine1));

        ResponseEntity<String> response = restTemplate.exchange("/api/purchase-order-line/delete/1",
                HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Purchase Order Line with ID 1 has been deleted");
    }
}
