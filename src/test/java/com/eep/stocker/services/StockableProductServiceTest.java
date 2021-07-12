package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IStockTransactionRepository;
import com.eep.stocker.repository.IStockableProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class StockableProductServiceTest {
    @Mock
    private IStockableProductRepository stockableProductRepository;

    @Mock
    IStockTransactionRepository stockTransactionRepository;

    StockableProductService stockableProductService;

    private StockableProduct mf220;
    private StockableProduct ov12;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        stockableProductService = new StockableProductService(stockableProductRepository, stockTransactionRepository);

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
    }

    @Test
    void testUpdateWithNewMpn() {
        given(stockableProductRepository.save(any(StockableProduct.class)))
                .willThrow(PersistenceException.class);

        assertThrows(MpnNotUniqueException.class, () -> stockableProductService.updateStockableProduct(mf220));
    }
}
