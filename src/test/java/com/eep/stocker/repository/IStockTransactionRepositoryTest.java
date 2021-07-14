package com.eep.stocker.repository;

import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class IStockTransactionRepositoryTest {
    private StockableProduct mf220;
    private StockableProduct ov12;

    private StockTransaction stockTransaction1;
    private StockTransaction stockTransaction2;
    private StockTransaction stockTransaction3;

    @Autowired
    private IStockTransactionRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        mf220 = new StockableProduct();
        //mf220.setId(1L);
        mf220.setName("MF220-M");
        mf220.setUnits("Flanges");
        mf220.setStockPrice(1.25D);
        mf220.setInStock(35.D);
        mf220.setDescription("8mm Mild Steel Flange");
        mf220.setCategory("Flange");
        mf220.setMpn("EEP210919001");


        ov12 = new StockableProduct();
        //ov12.setId(2L);
        ov12.setName("OV12");
        ov12.setUnits("Olives");
        ov12.setStockPrice(1.78D);
        ov12.setInStock(75.D);
        ov12.setDescription("Aluminized Olive");
        ov12.setCategory("Olive");
        ov12.setMpn("EEP210919002");

        stockTransaction1 = new StockTransaction();
        stockTransaction1.setStockableProduct(mf220);
        stockTransaction1.setQuantity(25);
        stockTransaction1.setReference("PO001");
        stockTransaction1.setNote("Created on " + LocalDate.now());

        stockTransaction2 = new StockTransaction();
        stockTransaction2.setStockableProduct(ov12);
        stockTransaction2.setQuantity(250);
        stockTransaction2.setReference("PO002");
        stockTransaction2.setNote("Created on " + LocalDate.now());

        stockTransaction3 = new StockTransaction();
        stockTransaction3.setStockableProduct(ov12);
        stockTransaction3.setQuantity(50);
        stockTransaction3.setReference("PO003");
        stockTransaction3.setNote("Created on " + LocalDate.now());
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void getAllStockTransactionsTest() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);
        stockTransaction1 = testEntityManager.persistFlushFind(stockTransaction1);
        stockTransaction2 = testEntityManager.persistFlushFind(stockTransaction2);

        List<StockTransaction> stx = repository.findAll();

        assertThat(stx).size().isEqualTo(2);
        assertThat(stx).contains(stockTransaction1, stockTransaction2);
    }

    @Test
    public void getAllStockTransactionsForStockableProductTest() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);
        stockTransaction1 = testEntityManager.persistFlushFind(stockTransaction1);
        stockTransaction2 = testEntityManager.persistFlushFind(stockTransaction2);
        stockTransaction3 = testEntityManager.persistFlushFind(stockTransaction3);

        List<StockTransaction> stx = repository.findAllByStockableProduct(ov12);

        assertThat(stx).size().isEqualTo(2);
        assertThat(stx).contains(stockTransaction2, stockTransaction3);
    }

    @Test
    public void sumAllStockTransactionsForStockableTest() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);
        stockTransaction1 = testEntityManager.persistFlushFind(stockTransaction1);
        stockTransaction2 = testEntityManager.persistFlushFind(stockTransaction2);
        stockTransaction3 = testEntityManager.persistFlushFind(stockTransaction3);

        Optional<Double> sumOfOv12Transactions = repository.getSumOfStockTransactionsForStockableProduct(ov12);
        Optional<Double> sumOfmf220Transactions = repository.getSumOfStockTransactionsForStockableProduct(mf220);

        assertThat(sumOfOv12Transactions).isPresent();
        assertThat(sumOfmf220Transactions).isPresent();
        assertThat(sumOfOv12Transactions).get().isEqualTo(300.0);
        assertThat(sumOfmf220Transactions).get().isEqualTo(25.0);
    }

    @Test
    public void sumAllStockTransacationsForStockableProductByIdTest() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);
        stockTransaction1 = testEntityManager.persistFlushFind(stockTransaction1);
        stockTransaction2 = testEntityManager.persistFlushFind(stockTransaction2);
        stockTransaction3 = testEntityManager.persistFlushFind(stockTransaction3);

        Optional<Double> sumOfOv12Transactions = repository.getSumOfStockTransactionsForStockableProductById(ov12.getId());
        Optional<Double> sumOfmf220Transactions = repository.getSumOfStockTransactionsForStockableProductById(mf220.getId());

        assertThat(sumOfOv12Transactions).isPresent();
        assertThat(sumOfmf220Transactions).isPresent();
        assertThat(sumOfOv12Transactions).get().isEqualTo(300.0);
        assertThat(sumOfmf220Transactions).get().isEqualTo(25.0);
    }

    @Test
    public void sumAllStockReturnsZeroForNoTransactions() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);

        Optional<Double> sumOfOv12Transactions = repository.getSumOfStockTransactionsForStockableProduct(ov12);

        assertThat(sumOfOv12Transactions).isNotPresent();
    }
}