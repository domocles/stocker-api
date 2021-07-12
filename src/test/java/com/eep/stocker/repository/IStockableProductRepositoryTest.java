package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class IStockableProductRepositoryTest {
    private StockableProduct mf220;
    private StockableProduct ov12;

    @Autowired
    private IStockableProductRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setup() {
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
    void canGetAllIdsTest() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);

        List<Long> ids = repository.findAllIdsForStockableProduct();

        assertThat(mf220.getId()).isNotNull();
        assertThat(mf220.getId()).isNotEqualTo(0);
        assertThat(ov12.getId()).isNotNull();
        assertThat(ov12.getId()).isNotEqualTo(0);

        assertThat(ids).size().isEqualTo(2);
        assertThat(ids).contains(mf220.getId(), ov12.getId());
    }

    @Test
    void updateStockableProductWithSameMpn() {
        mf220 = testEntityManager.persistFlushFind(mf220);
        ov12 = testEntityManager.persistFlushFind(ov12);

        StockableProduct mf264 = StockableProduct.builder()
                .name("MF264")
                .mpn("EEP210919001")
                .category("Flanges")
                .units("Flange")
                .build();

        Exception exception = assertThrows(PersistenceException.class, () -> testEntityManager.persistFlushFind(mf264));
    }
}