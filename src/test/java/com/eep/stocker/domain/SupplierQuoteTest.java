package com.eep.stocker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SupplierQuoteTest {

    private StockableProduct mf220;
    private Supplier shelleys;
    private SupplierQuote mf220quote;

    @BeforeEach
    void setUp() {
        mf220 = new StockableProduct();
        mf220.setId(1L);
        mf220.setMpn("EEP123");
        mf220.setCategory("Flange");
        mf220.setDescription("A flange");
        mf220.setInStock(25.);
        mf220.setStockPrice(1.26);
        mf220.setUnits("Flanges");
        mf220.setName("MF220");

        shelleys = new Supplier();
        shelleys.setId(1L);
        shelleys.setTelephoneNumber("01527 578686");
        shelleys.setEmailAddress("jon.horton@shelleys.co.uk");
        shelleys.setDefaultCurrency("GBP");
        shelleys.setSupplierName("Shelley Parts Ltd");

        mf220quote = new SupplierQuote();
        mf220quote.setPrice(1.25);
        mf220quote.setQty(15.);
        mf220quote.setStockableProduct(mf220);
        mf220quote.setSupplier(shelleys);
        mf220quote.setId(1L);
        mf220quote.setQuotationDate(new Date());
    }

    @Test
    void testEquals() {
        SupplierQuote mf220quote2 = new SupplierQuote();
        mf220quote2.setPrice(1.25);
        mf220quote2.setQty(15.);
        mf220quote2.setStockableProduct(mf220);
        mf220quote2.setSupplier(shelleys);
        mf220quote2.setId(1L);
        mf220quote2.setQuotationDate(mf220quote.getQuotationDate());

        assertThat(mf220quote).isEqualTo(mf220quote);
        assertThat(mf220quote).isEqualTo(mf220quote2);
    }

    @Test
    void testListContains() {
        List<SupplierQuote> quotes = new ArrayList<>();
        quotes.add(mf220quote);

        assertThat(quotes).contains(mf220quote);
    }
}