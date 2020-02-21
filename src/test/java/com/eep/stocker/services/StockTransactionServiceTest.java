package com.eep.stocker.services;

import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IStockTransactionRepository;
import com.eep.stocker.repository.IStockableProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class StockTransactionServiceTest {
    @Mock
    private IStockTransactionRepository stockTransactionRepository;

    @Mock
    private IStockableProductRepository stockableProductRepository;

    private StockTransactionService stockTransactionService;

    private StockableProduct mf220;
    private StockableProduct ov12;

    private StockTransaction stockTransaction1;
    private StockTransaction stockTransaction2;
    private StockTransaction stockTransaction3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.stockTransactionService = new StockTransactionService(stockTransactionRepository, stockableProductRepository);

        mf220 = new StockableProduct();
        mf220.setId(1L);
        mf220.setName("MF220-M");
        mf220.setUnits("Flanges");
        mf220.setStockPrice(1.25D);
        mf220.setInStock(35.D);
        mf220.setDescription("8mm Mild Steel Flange");
        mf220.setCategory("Flange");
        mf220.setMpn("EEP210919001");


        ov12 = new StockableProduct();
        ov12.setId(2L);
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

    @Test
    void getAllStockTransactions() {
        given(stockTransactionRepository.findAll()).willReturn(Arrays.asList(stockTransaction1, stockTransaction2, stockTransaction3));

        List<StockTransaction> allTransactions = stockTransactionService.getAllStockTransactions();

        assertThat(allTransactions.size()).isEqualTo(3);
        assertThat(allTransactions).contains(stockTransaction1, stockTransaction2, stockTransaction3);
    }

    @Test
    public void getStockTransactionByIdTest() {
        given(stockTransactionRepository.findById(anyLong())).willReturn(Optional.of(stockTransaction2));

        Optional<StockTransaction> stockTransaction = stockTransactionService.getStockTransactionById(3L);

        assertThat(stockTransaction).isPresent();
        assertThat(stockTransaction).get().isEqualTo(stockTransaction2);
    }

    @Test
    public void getStockTransactionsForStockableProductTest() {
        given(stockTransactionRepository.findAllByStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(stockTransaction2, stockTransaction3));

        List<StockTransaction> stockTransactionsForStockableProduct
                = stockTransactionService.getAllStockTransactionsForStockableProduct(ov12);

        assertThat(stockTransactionsForStockableProduct.size()).isEqualTo(2);
        assertThat(stockTransactionsForStockableProduct).contains(stockTransaction2, stockTransaction3);
    }

    @Test
    public void getSumOfTransactionsForStockableProductTest() {
        given(stockTransactionRepository.getSumOfStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(Optional.of(50.0));

        double stockTransactionBalance = stockTransactionService
                .getStockTransactionBalanceForStockableProduct(ov12);

        assertThat(stockTransactionBalance).isEqualTo(50.0);
    }

    @Test
    public void getSumOfTransactionsForStockableProductWithNoTransactionsTest() {
        given(stockTransactionRepository.getSumOfStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(Optional.empty());

        double stockTransactionBalance = stockTransactionService
                .getStockTransactionBalanceForStockableProduct(ov12);

        assertThat(stockTransactionBalance).isEqualTo(0.0);
    }

    @Test
    public void getAllStockBalancesTest() {
        given(stockableProductRepository.findAllIdsForStockableProduct()).willReturn(Arrays.asList(1L, 2L));
        given(stockTransactionRepository.getSumOfStockTransactionsForStockableProductById(1L)).willReturn(Optional.of(50.0));
        given(stockTransactionRepository.getSumOfStockTransactionsForStockableProductById(2L)).willReturn(Optional.of(150.0));

        Map<Long, Double> stockBalances = stockTransactionService.getStockTransactionBalanceForAllStockableProducts();

        assertThat(stockBalances.size()).isEqualTo(2);
        assertThat(stockBalances.containsKey(1L));
        assertThat(stockBalances.get(1L)).isEqualTo(50);

        assertThat(stockBalances.containsKey(2L));
        assertThat(stockBalances.get(2L)).isEqualTo(150);
    }
}