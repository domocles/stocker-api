package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.dto.supplierquote.*;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
import com.eep.stocker.testdata.SupplierTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierQuoteControllerTest extends SupplierTestData {

    @MockBean
    private SupplierQuoteService supplierQuoteService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private StockableProductService stockableProductService;

    @Autowired
    private SupplierQuoteMapper supplierQuoteMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private SupplierQuote getSupplierQuote() {

        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, LocalDate.now(), 15.0, 1.72D);
        return quote;
    }

    @Test
    void getAllSuppliersTest() throws Exception {
        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, LocalDate.now(), 15.0, 1.72D);
        given(supplierQuoteService.getAllSupplierQuotes())
                .willReturn(Arrays.asList(quote));

        var response  = restTemplate.exchange(
                "/api/supplier-quote/",
                HttpMethod.GET,
                null,
                GetAllSupplierQuotesResponse.class);

        var testRes = supplierQuoteMapper.mapToLowDetailResponse(quote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().supplierQuotes).contains(testRes)
        );

    }

    @Test
     void getSupplierQuoteByIdTest() {
        var quote = getSupplierQuote();
        given(supplierQuoteService.getSupplierQuoteByUid(anyString()))
                .willReturn(java.util.Optional.of(quote));

        var response = restTemplate.exchange(
                "/api/supplier-quote/" + getSupplierQuote().getUid().toString(),
                HttpMethod.GET,
                null,
                GetSupplierQuoteResponse.class
        );

        var testRes = supplierQuoteMapper.mapToGetResponse(quote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testRes)
        );
    }

    @Test
    void getSupplierQuotesForNonExistantSuppliersTest() {
        given(supplierQuoteService.getAllSupplierQuotesForSupplier(any(Supplier.class)))
                .willThrow(new SupplierDoesNotExistException("Supplier Does Not Exist!"));

        var response = restTemplate.exchange(
                "/api/supplier-quote/supplier/" + supplier.getUid().toString(),
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
        );
    }

    @Test
    void getSupplierQuotesForSupplierTest() throws Exception {

        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, LocalDate.now(), 15.0, 1.72D);

        given(supplierQuoteService.getAllSupplierQuotesForSupplier(any(Supplier.class)))
                .willReturn(Arrays.asList(quote));
        given(supplierService.getSupplierFromUid(anyString())).willReturn(java.util.Optional.of(supplier));

        var response = restTemplate.exchange(
                "/api/supplier-quote/supplier/" + supplier.getUid().toString() + "/",
                HttpMethod.GET,
                null,
                GetSupplierQuotesForSupplierResponse.class
        );

        var testRes = supplierQuoteMapper.mapToLowDetailResponse(quote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().supplierQuotes).contains(testRes)
        );
    }

    @Test
    void getSupplierQuotesForStockableProductTest() {
        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, LocalDate.now(), 15.0, 1.72D);

        given(stockableProductService.getStockableProductByUid(anyString()))
                .willReturn(java.util.Optional.of(stockableProduct));

        given(supplierQuoteService.getAllSupplierQuotesForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(quote));

        var response = restTemplate.exchange(
                "/api/supplier-quote/stockable-product/" + stockableProduct.getUid().toString() + "/",
                HttpMethod.GET,
                null,
                GetSupplierQuotesForStockableProductResponse.class
        );

        var testRes = supplierQuoteMapper.mapToLowDetailResponse(quote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().supplierQuotes).contains(testRes)
        );
    }

    @Test
    void updateSupplierQuoteTest() {
        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, LocalDate.now(), 15.0, 1.72D);

        given(supplierQuoteService.updateSupplierQuote(any(SupplierQuote.class))).willReturn(quote);
    }
}
