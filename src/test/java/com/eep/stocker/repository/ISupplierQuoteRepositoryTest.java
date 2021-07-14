package com.eep.stocker.repository;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ISupplierQuoteRepositoryTest {

    @Autowired
    private ISupplierQuoteRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    Supplier shelleys;
    Supplier cls;
    StockableProduct mf220;
    StockableProduct ov12;
    SupplierQuote mf220Quote;
    SupplierQuote mf220Quote2;
    SupplierQuote mf220Quote3;
    SupplierQuote ov12Quote;

    @BeforeEach
    private void initialize() {
        shelleys = new Supplier();
        //shelleys.setId(1L);
        shelleys.setSupplierName("Shelley Parts Ltd");
        shelleys.setDefaultCurrency("GBP");
        shelleys.setEmailAddress("jon.horton@shelleyparts.co.uk");
        shelleys.setTelephoneNumber("01384 956541");

        cls = new Supplier();
        cls.setSupplierName("Central Laser Services Ltd");
        cls.setDefaultCurrency("GBP");
        cls.setEmailAddress("info@cls.co.uk");
        cls.setTelephoneNumber("01527 584285");


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


        mf220Quote = new SupplierQuote();
        mf220Quote.setSupplier(shelleys);
        mf220Quote.setStockableProduct(mf220);
        mf220Quote.setQty(15.0D);
        mf220Quote.setPrice(1.27D);
        mf220Quote.setQuotationDate(new Date(2019, 9, 20));

        mf220Quote2 = new SupplierQuote();
        mf220Quote2.setSupplier(cls);
        mf220Quote2.setStockableProduct(mf220);
        mf220Quote2.setQty(15.0D);
        mf220Quote2.setPrice(1.15D);
        mf220Quote2.setQuotationDate(new Date(2019, 8, 22));

        mf220Quote3 = new SupplierQuote();
        mf220Quote3.setSupplier(shelleys);
        mf220Quote3.setStockableProduct(mf220);
        mf220Quote3.setQty(15.0D);
        mf220Quote3.setPrice(1.27D);
        mf220Quote3.setQuotationDate(new Date(2019, 9, 22));

        ov12Quote = new SupplierQuote();
        ov12Quote.setSupplier(shelleys);
        ov12Quote.setStockableProduct(ov12);
        ov12Quote.setQty(75.0D);
        ov12Quote.setPrice(0.87D);

    }

    @Test
    void findBySupplierTest() {
        shelleys = entityManager.persistFlushFind(shelleys);
        mf220 = entityManager.persistFlushFind(mf220);
        ov12 = entityManager.persistFlushFind(ov12);
        mf220Quote = entityManager.persistFlushFind(mf220Quote);
        ov12Quote = entityManager.persistFlushFind(ov12Quote);

        List<SupplierQuote> quotes = repository.findBySupplier(shelleys);

        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes.contains(mf220Quote)).isTrue();
        assertThat(quotes).contains(ov12Quote);
    }

    @Test
    void findByStockableProductTest() {
        shelleys = entityManager.persistFlushFind(shelleys);
        cls = entityManager.persistFlushFind(cls);
        mf220 = entityManager.persistFlushFind(mf220);
        ov12 = entityManager.persistFlushFind(ov12);
        mf220Quote = entityManager.persistFlushFind(mf220Quote);
        mf220Quote2 = entityManager.persistFlushFind(mf220Quote2);
        ov12Quote = entityManager.persistFlushFind(ov12Quote);

        List<SupplierQuote> quotes = repository.findByStockableProduct(mf220);

        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes).contains(mf220Quote);
        assertThat(quotes).contains(mf220Quote2);
    }

    @Test
    void findTopByStockableProductAndSupplierOrderByQuotationDateDesc() {
        shelleys = entityManager.persistFlushFind(shelleys);
        cls = entityManager.persistFlushFind(cls);
        mf220 = entityManager.persistFlushFind(mf220);
        ov12 = entityManager.persistFlushFind(ov12);
        mf220Quote = entityManager.persistFlushFind(mf220Quote);
        mf220Quote2 = entityManager.persistFlushFind(mf220Quote2);
        mf220Quote3 = entityManager.persistFlushFind(mf220Quote3);

        SupplierQuote quote = repository.findTopByStockableProductAndSupplierOrderByQuotationDateDesc(mf220, shelleys);

        assertThat(quote).isEqualTo(mf220Quote3);
    }

    @Test
    void findAllByNonExistantSupplierTest() {
        shelleys = entityManager.persistFlushFind(shelleys);
        List<SupplierQuote> quotes = repository.findBySupplier(shelleys);

        assertThat(quotes).isNotNull();
        assertThat(quotes.size()).isEqualTo(0);
    }
}