package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
import org.junit.Assert;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierQuoteControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private SupplierQuoteService supplierQuoteService;

    @Test
    void getSupplierQuoteTest() {
        //arrange
        /*Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

        StockableProduct stockableProduct = new StockableProduct(1L, "MF220",
                "EEP200919001",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.72D,
                25.0D);

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, new Date(), 15.0, 1.72D);
        given(supplierService.getSupplierFromId(anyLong())).willReturn(Optional.of(supplier));
        given(supplierQuoteService.getAllSupplierQuotesForSupplier(any(Supplier.class))).willReturn(Arrays.asList(quote));

        //act
        ResponseEntity<SupplierQuote> response = restTemplate.getForEntity("/api/supplier-quote/get/1", SupplierQuote.class);

        //assert
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        Assert.assertTrue(response.getBody().getSupplier().getSupplierName().equals("Shelley Parts UK Ltd"));*/
    }

    @Test
    void getSupplierQuotesForSupplier() {

    }

    @Test
    void getSupplierQuotesForStockableProduct() {
    }
}