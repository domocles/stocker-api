package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.SupplierQuote;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierQuoteControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getSupplierQuote() {
        //arrange

        //act
        ResponseEntity<SupplierQuote> response = restTemplate.getForEntity("/api/supplier-quote/get/1", SupplierQuote.class);

        //assert
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        Assert.assertTrue(response.getBody().getSupplier().getSupplierName().equals("Shelley Parts UK Ltd"));
    }

    @Test
    void getSupplierQuotesForSupplier() {

    }

    @Test
    void getSupplierQuotesForStockableProduct() {
    }
}