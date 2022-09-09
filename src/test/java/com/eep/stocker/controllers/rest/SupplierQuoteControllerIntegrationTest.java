package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.dto.supplierquote.*;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierQuoteControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SupplierQuoteMapper mapper;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private SupplierQuoteService supplierQuoteService;

    @MockBean
    private StockableProductService stockableProductService;

    StockableProduct mf220;
    StockableProduct ov12;

    SupplierQuote supplierQuote;
    SupplierQuote supplierQuote2;

    @BeforeEach
    public void setup() {
        mf220 = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flange")
                .tag("2 inch")
                .units("Flanges")
                .stockPrice(1.72D)
                .inStock(25.0D)
                .build();

        ov12 = StockableProduct.builder()
                .id(2L)
                .name("OV12")
                .mpn("EEP200919002")
                .description("Aluminised Olive")
                .category("Olives")
                .tag("1-inch")
                .units("Olives")
                .stockPrice(0.75D)
                .inStock(50.0D)
                .build();

        supplierQuote = SupplierQuote.builder()
                .supplier(shelleys)
                .stockableProduct(mf220)
                .quotationDate(LocalDate.now())
                .qty(15.0)
                .price(1.72D)
                .build();

        supplierQuote2 = SupplierQuote.builder()
                .supplier(supplier)
                .stockableProduct(ov12)
                .quotationDate(LocalDate.now())
                .qty(75.0)
                .price(0.75D)
                .build();
    }

    @Test
    void getSupplierQuoteByIdTest() {
        given(supplierQuoteService.getSupplierQuoteByUid(anyString())).willReturn(Optional.of(supplierQuote));

        var response = restTemplate.exchange(
                "/api/supplier-quote/" + supplierQuote.getUid().toString(),
                HttpMethod.GET,
                null,
                GetSupplierQuoteResponse.class
        );

        var testResponse = mapper.mapToGetResponse(supplierQuote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void getAllSupplierQuotesTest() {
        given(supplierQuoteService.getAllSupplierQuotes()).willReturn(List.of(supplierQuote, supplierQuote2));

        var res = restTemplate.exchange(
            "/api/supplier-quote/",
                HttpMethod.GET,
                null,
                GetAllSupplierQuotesResponse.class
        );

        var testQuote1 = mapper.mapToLowDetailResponse(supplierQuote);
        var testQuote2 = mapper.mapToLowDetailResponse(supplierQuote2);

        assertAll(
                () -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(res.getBody()).supplierQuotes).contains(testQuote1, testQuote2)
        );
    }

    @Test
    void getSupplierQuotesForSupplierTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(shelleys));
        given(supplierQuoteService.getAllSupplierQuotesForSupplier(any(Supplier.class))).willReturn(List.of(supplierQuote, supplierQuote2));

        var response = restTemplate.exchange(
                "/api/supplier-quote/supplier/" + supplier.getUid().toString() + "/",
                HttpMethod.GET,
                null,
                GetSupplierQuotesForSupplierResponse.class
        );

        var testQuote1 = mapper.mapToLowDetailResponse(supplierQuote);
        var testQuote2 = mapper.mapToLowDetailResponse(supplierQuote2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).supplierQuotes).contains(testQuote1, testQuote2)
        );
    }

    @Test
    void getSupplierQuotesForStockableProduct() {
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(mf220));
        given(supplierQuoteService.getAllSupplierQuotesForStockableProduct(any(StockableProduct.class)))
                .willReturn(List.of(supplierQuote));

        var response = restTemplate.exchange(
                "/api/supplier-quote/stockable-product/" + mf220.getUid().toString() + "/",
                    HttpMethod.GET,
                null,
                    GetSupplierQuotesForStockableProductResponse.class
        );

        var testRes = mapper.mapToLowDetailResponse(supplierQuote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).supplierQuotes).contains(testRes)
        );
    }

    @Test
    void deleteSupplierQuoteTest() {
        given(supplierQuoteService.deleteSupplierQuoteByUid(anyString())).willReturn(Optional.of(supplierQuote));

        var response = restTemplate.exchange(
                "/api/supplier-quote/" + supplierQuote.getUid().toString(),
                HttpMethod.DELETE,
                null,
                DeleteSupplierQuoteResponse.class
        );

        var testRes = mapper.mapToDeleteResponse(supplierQuote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testRes)
        );
    }

    @Test
    void createNewSupplierQuoteTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplierQuote.getSupplier()));
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(supplierQuote.getStockableProduct()));
        given(supplierQuoteService.saveSupplierQuote(any(SupplierQuote.class))).willReturn(supplierQuote);

        var request = CreateSupplierQuoteRequest.builder()
                .supplierId(supplierQuote.getUid().toString())
                .stockableProductId(supplierQuote.getStockableProduct().getUid().toString())
                .qty(supplierQuote.getQty())
                .price(supplierQuote.getPrice())
                .quotationDate(supplierQuote.getQuotationDate())
                .build();

        var response = restTemplate.postForEntity(
            "/api/supplier-quote/",
            request,
            CreateSupplierQuoteResponse.class
        );

        var testRes = mapper.mapToCreateResponse(supplierQuote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testRes)
        );
    }

    @Test
    void updateSupplierQuoteTest() {
        var supplierQuoteUpdated = supplierQuote.toBuilder().qty(1000.0).build();
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplierQuote.getSupplier()));
        given(stockableProductService.getStockableProductByUid(anyString()))
                .willReturn(Optional.of(supplierQuote.getStockableProduct()));
        given(supplierQuoteService.getSupplierQuoteByUid(anyString())).willReturn(Optional.of(supplierQuote));
        given(supplierQuoteService.saveSupplierQuote(any(SupplierQuote.class))).willReturn(supplierQuoteUpdated);

        var request = UpdateSupplierQuoteRequest.builder()
                .supplierId(supplierQuoteUpdated.getSupplier().getUid().toString())
                .stockableProductId(supplierQuote.getStockableProduct().getUid().toString())
                .qty(1000.0)
                .price(supplierQuote.getPrice())
                .quotationDate(supplierQuote.getQuotationDate())
                .build();

        var response = restTemplate.exchange(
                "/api/supplier-quote/"+supplierQuote.getUid(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateSupplierQuoteResponse.class
        );

        var testResponse= mapper.mapToUpdateResponse(supplierQuoteUpdated);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }
}