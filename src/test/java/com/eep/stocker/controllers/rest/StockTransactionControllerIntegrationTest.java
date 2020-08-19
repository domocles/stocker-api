package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.StockTransactionService;
import com.eep.stocker.services.StockableProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockTransactionControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StockTransactionService stockTransactionService;

    @MockBean
    private StockableProductService stockableProductService;

    private StockTransaction stockTransaction;
    private StockTransaction stockTransaction2;
    private StockTransaction stockTransaction3;

    private StockableProduct mf220;
    private StockableProduct mf236;

    @BeforeEach
    void setUp() {
        stockTransaction = new StockTransaction();
        stockTransaction2 = new StockTransaction();
        stockTransaction3 = new StockTransaction();

        mf220 = new StockableProduct();
        mf220.setId(1L);
        mf220.setMpn("EEP123");
        mf220.setCategory("Flange");
        mf220.setDescription("A flange");
        mf220.setInStock(25.);
        mf220.setStockPrice(1.26);
        mf220.setUnits("Flanges");
        mf220.setName("MF220");

        mf236 = new StockableProduct();
        mf236.setId(2L);
        mf236.setMpn("EEP456");
        mf236.setCategory("Flange");
        mf236.setDescription("A flange");
        mf236.setInStock(15.);
        mf236.setStockPrice(1.26);
        mf236.setUnits("Flanges");
        mf236.setName("MF236");

        stockTransaction.setStockableProduct(mf220);
        stockTransaction2.setStockableProduct(mf220);
        stockTransaction3.setStockableProduct(mf236);
    }

    @Test
    void getAllStockTransactionsTest() {
        //arrange
        given(stockTransactionService.getAllStockTransactions()).willReturn(Arrays.asList(stockTransaction, stockTransaction2, stockTransaction3));
        //act
        ResponseEntity<List<StockTransaction>> response = restTemplate.exchange("/api/stock-transaction/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockTransaction>>() {  });

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains(stockTransaction, stockTransaction2, stockTransaction3);
    }

    @Test
    void getStockTransactionByIdTest() {
        given(stockTransactionService.getStockTransactionById(any(Long.class))).willReturn(Optional.of(stockTransaction2));

        ResponseEntity<StockTransaction> response = restTemplate.exchange("/api/stock-transaction/get/5",
                HttpMethod.GET,
                null,
                StockTransaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(stockTransaction2);
    }

    @Test
    void getStockTransactionForTransactionThatDoesntExistThrowsError() {
        given(stockTransactionService.getStockTransactionById(any(Long.class))).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/stock-transaction/get/5",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails().get(0)).isEqualTo("Stock transaction with ID of 5 does not exist");
    }

    @Test
    void getStockTransactionsForStockableProductTest() {
        given(stockTransactionService.getAllStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(stockTransaction2, stockTransaction3));
        given(stockableProductService.getStockableProductByID(anyLong())).willReturn(Optional.of(mf220));

        ResponseEntity<List<StockTransaction>> response = restTemplate.exchange("/api/stock-transaction/stockable-product/get/4",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockTransaction>>() {  });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains(stockTransaction2, stockTransaction3);
    }

    @Test
    void getStockTransactionsForStockableProductThatDoesntExistThrowsException() {
        given(stockTransactionService.getAllStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(stockTransaction2, stockTransaction3));
        given(stockableProductService.getStockableProductByID(anyLong())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/stock-transaction/stockable-product/get/4",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails().get(0)).isEqualTo("Stockable product with id of 4 does not exist");
    }

    @Test
    void createStockTransactionTest() {
        StockTransaction unsavedStockTransaction = new StockTransaction();
        unsavedStockTransaction.setStockableProduct(mf236);
        unsavedStockTransaction.setNote("Test stock transaction");
        unsavedStockTransaction.setReference("TEST");
        unsavedStockTransaction.setQuantity(200);
        unsavedStockTransaction.setDateCreated(LocalDate.now());

        StockTransaction savedStockTransaction = new StockTransaction();
        savedStockTransaction.setStockableProduct(mf236);
        savedStockTransaction.setNote("Test stock transaction");
        savedStockTransaction.setReference("TEST");
        savedStockTransaction.setQuantity(200);
        savedStockTransaction.setDateCreated(LocalDate.now());

        given(stockTransactionService.saveStockTransaction(unsavedStockTransaction)).willReturn(savedStockTransaction);

        ResponseEntity<StockTransaction> response = restTemplate.postForEntity("/api/stock-transaction/create",
                unsavedStockTransaction,
                StockTransaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(savedStockTransaction);
    }

    @Test
    void testSendWrongObjectTypeToSaveTest() {
        //given(stockTransactionService.saveStockTransaction(unsavedStockTransaction)).willReturn(savedStockTransaction);

        ResponseEntity<StockTransaction> response = restTemplate.postForEntity("/api/stock-transaction/create",
                mf220,
                StockTransaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testDeleteStockTransaction() {
        given(stockTransactionService.getStockTransactionById(anyLong())).willReturn(Optional.of(stockTransaction));
        given(stockTransactionService.deleteStockTransaction(any(StockTransaction.class))).willReturn(stockTransaction);

        ResponseEntity<StockTransaction> response = restTemplate.exchange("/api/stock-transaction/delete/4",
                HttpMethod.DELETE,
                null,
                StockTransaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(stockTransaction);
    }

    @Test
    void testDeleteNonExistantStockTransactionThrowsError() {
        given(stockTransactionService.getStockTransactionById(anyLong())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/stock-transaction/delete/4",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails().get(0)).isEqualTo("StockTransaction with id of 4 does not exist");
    }

    @Test
    void testGetBalanceOfStockForStockableProduct() {
        given(stockableProductService.getStockableProductByID(5L)).willReturn(Optional.of(mf220));
        given(stockableProductService.getStockableProductByID(6L)).willReturn(Optional.of(mf236));
        given(stockTransactionService.getStockTransactionBalanceForStockableProduct(mf220))
                .willReturn(50.0);
        given(stockTransactionService.getStockTransactionBalanceForStockableProduct(mf236))
                .willReturn(100.0);

        ResponseEntity<Double> response = restTemplate.exchange("/api/stock-transaction/balance/5",
                HttpMethod.GET,
                null,
                Double.class);

        ResponseEntity<Double> response2 = restTemplate.exchange("/api/stock-transaction/balance/6",
                HttpMethod.GET,
                null,
                Double.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(50);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody()).isEqualTo(100);
    }

    @Test
    void testGetBalanceForNonExistantStockableProductThrowsExceptionTest() {
        given(stockableProductService.getStockableProductByID(anyLong())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/stock-transaction/balance/6",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails().get(0)).isEqualTo("Stockable product with id of 6 does not exist");
    }
}