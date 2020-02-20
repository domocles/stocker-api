package com.eep.stocker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StockTransactionTest {
    private StockTransaction stockTransaction;
    private StockTransaction stockTransaction2;
    private StockTransaction stockTransaction3;

    private StockableProduct mf220;
    private StockableProduct mf236;

    @BeforeEach
    void setup() {
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
    public void testEquals() {
        assertThat(stockTransaction).isEqualTo(stockTransaction2);
        assertThat(stockTransaction).isNotEqualTo(stockTransaction3);
    }
}
