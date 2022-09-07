package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import com.eep.stocker.dto.stocktransaction.*;
import com.eep.stocker.services.StockTransactionService;
import com.eep.stocker.services.StockableProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockTransactionControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StockTransactionMapper mapper;

    @Autowired
    private StockableProductMapper stockableProductMapper;

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
        ResponseEntity<GetAllStockTransactionsResponse> response = restTemplate.exchange("/api/stock-transaction/",
                HttpMethod.GET,
                null,
                GetAllStockTransactionsResponse.class);

        var testStockTransaction1 = mapper.mapToLowDetailResponse(stockTransaction);
        var testStockTransaction2 = mapper.mapToLowDetailResponse(stockTransaction2);
        var testStockTransaction3 = mapper.mapToLowDetailResponse(stockTransaction3);

        //assert
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().allStockTransactions).contains(testStockTransaction1, testStockTransaction2, testStockTransaction3)
        );
    }

    @Test
    void getStockTransactionByIdTest() {
        given(stockTransactionService.getStockTransactionByUid(anyString())).willReturn(Optional.of(stockTransaction2));

        ResponseEntity<GetStockTransactionResponse> response = restTemplate.exchange("/api/stock-transaction/" + stockTransaction2.getUid().toString(),
                HttpMethod.GET,
                null,
                GetStockTransactionResponse.class);

        var testResponse = mapper.mapToGetResponse(stockTransaction2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void getStockTransactionForTransactionThatDoesntExistThrowsError() {
        given(stockTransactionService.getStockTransactionByUid(anyString())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/stock-transaction/" + stockTransaction2.getUid().toString(),
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().getDetails().get(0)).isEqualTo("Stock Transaction Does Not Exist")
        );
    }

    @Test
    void getStockTransactionsForStockableProductTest() {
        given(stockTransactionService.getAllStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(stockTransaction2, stockTransaction3));
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(mf220));

        var response = restTemplate.exchange(
                "/api/stock-transaction/stockable-product/" + stockTransaction.getUid().toString(),
                HttpMethod.GET,
                null,
                GetStockTransactionsByStockableProductResponse.class);

        var testStockTransaction2 = mapper.mapToLowDetailResponse(stockTransaction2);
        var testStockTransaction3 = mapper.mapToLowDetailResponse(stockTransaction3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().allStockTransactions).contains(testStockTransaction2, testStockTransaction3)
        );
    }

    @Test
    void getStockTransactionsForStockableProductThatDoesntExistThrowsException() {
        given(stockTransactionService.getAllStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(stockTransaction2, stockTransaction3));
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/stock-transaction/stockable-product/" + stockTransaction.getUid().toString(),
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().getDetails().get(0)).isEqualTo("Stockable Product Does Not Exist")
        );
    }

    @Test
    void createStockTransactionTest() {
        StockTransaction savedStockTransaction = new StockTransaction();
        savedStockTransaction.setStockableProduct(mf236);
        savedStockTransaction.setNote("Test stock transaction");
        savedStockTransaction.setReference("TEST");
        savedStockTransaction.setQuantity(200);

        var request = new CreateStockTransactionRequest();
        request.setStockableProductId(mf236.getUid().toString());
        request.setQuantity(200.0);
        request.setReference("TEST");
        request.setNote("Test stock transaction");

        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(mf236));
        given(stockTransactionService.saveStockTransaction(any(StockTransaction.class))).willReturn(savedStockTransaction);

        var response = restTemplate.postForEntity("/api/stock-transaction/",
                request,
                CreateStockTransactionResponse.class);

        var testResponse = mapper.mapToCreateResponse(savedStockTransaction);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void testSendWrongObjectTypeToSaveTest() {
        //given(stockTransactionService.saveStockTransaction(unsavedStockTransaction)).willReturn(savedStockTransaction);

        var response = restTemplate.postForEntity("/api/stock-transaction/",
                mf220,
                CreateStockTransactionResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteStockTransaction() {
        given(stockTransactionService.getStockTransactionByUid(anyString())).willReturn(Optional.of(stockTransaction));
        given(stockTransactionService.deleteStockTransaction(any(StockTransaction.class))).willReturn(stockTransaction);

        var response = restTemplate.exchange(
                "/api/stock-transaction/" + stockTransaction.getUid().toString(),
                HttpMethod.DELETE,
                null,
                String.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(String.format("Stock Transaction with id of %s has been deleted", stockTransaction.getUid().toString()))
        );
    }

    @Test
    void testDeleteNonExistantStockTransactionThrowsError() {
        given(stockTransactionService.getStockTransactionById(anyLong())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/stock-transaction/" + stockTransaction.getUid().toString(),
                HttpMethod.DELETE,
                null,
                ErrorResponse.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().getDetails().get(0))
                        .isEqualTo(String.format("StockTransaction with id of %s does not exist", stockTransaction.getUid().toString()))
        );
    }

    @Test
    void testGetBalanceOfStockForStockableProduct() {
        given(stockableProductService.getStockableProductByUid(mf220.getUid().toString())).willReturn(Optional.of(mf220));
        given(stockableProductService.getStockableProductByUid(mf236.getUid().toString())).willReturn(Optional.of(mf236));
        given(stockTransactionService.getStockTransactionBalanceForStockableProduct(mf220))
                .willReturn(50.0);
        given(stockTransactionService.getStockTransactionBalanceForStockableProduct(mf236))
                .willReturn(100.0);

        ResponseEntity<Double> response = restTemplate.exchange("/api/stock-transaction/balance/" + mf220.getUid().toString(),
                HttpMethod.GET,
                null,
                Double.class);

        ResponseEntity<Double> response2 = restTemplate.exchange("/api/stock-transaction/balance/" + mf236.getUid().toString(),
                HttpMethod.GET,
                null,
                Double.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(50),
                () -> assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response2.getBody()).isNotNull(),
                () -> assertThat(response2.getBody()).isEqualTo(100)
        );
    }

    @Test
    void testGetBalanceForNonExistantStockableProductThrowsExceptionTest() {
        given(stockableProductService.getStockableProductByID(anyLong())).willReturn(Optional.empty());

        var response = restTemplate.exchange(
                "/api/stock-transaction/balance/" + mf220.getUid().toString(),
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().getDetails().get(0))
                    .isEqualTo(String.format("Stockable Product Does Not Exist", mf220.getUid().toString()))
        );
    }

    @Test
    void testUpdateStockTransaction() {
        given(stockTransactionService.getStockTransactionByUid(anyString())).willReturn(Optional.of(stockTransaction));
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(mf236));

        var request = new UpdateStockTransactionRequest();
        request.setStockableProductId(mf236.getUid().toString());
        request.setQuantity(25.0);
        request.setReference(stockTransaction.getReference());
        request.setNote(stockTransaction.getNote());

        var response = restTemplate.exchange("/api/stock-transaction/" + stockTransaction.getUid(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateStockTransactionResponse.class);

        var testResponse = mapper.mapToUpdateResponse(stockTransaction);
        testResponse.setStockableProduct(stockableProductMapper.stockableProductResponseFromStockableProduct(mf236));
        testResponse.setQuantity(25.0);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }
}