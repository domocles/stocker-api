package com.eep.stocker.services;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.repository.ISupplierQuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
class SupplierQuoteServiceTest {

    @Mock
    private ISupplierQuoteRepository supplierQuoteRepository;

    private SupplierQuoteService supplierQuoteService;
    private SupplierQuote newSupplierQuote;
    private SupplierQuote savedSupplierQuote;
    private SupplierQuote MF286Quote;
    private SupplierQuote FJIQuote;
    private Supplier shelleys;
    private Supplier fji;
    private StockableProduct MF220;
    private StockableProduct MF286;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.supplierQuoteService = new SupplierQuoteService(supplierQuoteRepository);
        newSupplierQuote = getNewSupplierQuote().get();
        savedSupplierQuote = getSavedSupplierQuote().get();


        shelleys = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

        fji = new Supplier("FJI Industries",
                "EU",
                "info@fji.dk",
                "0030 456789");

        MF220 = new StockableProduct(1L, "MF220",
                "EEP200919001",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.72D,
                25.0D);

        MF286 = new StockableProduct(2L, "MF286",
                "EEP200919002",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.45D,
                75.D);

        MF286Quote = new SupplierQuote(MF286, shelleys, new Date(), 15.0, 2.76);
        FJIQuote = new SupplierQuote(MF220, fji, new Date(), 100.0, 1.96);
    }

    private static Optional<SupplierQuote> getNewSupplierQuote() {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
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
        return Optional.of(quote);
    }

    private static Optional<SupplierQuote> getSavedSupplierQuote() {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
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
        quote.setId(1L);
        return Optional.of(quote);
    }

    @Test
    void saveSupplierQuote() {
        assertThat(supplierQuoteService).isNotNull();

        SupplierQuote newSupplierQuote = getNewSupplierQuote().get();
        SupplierQuote savedSupplierQuote = getSavedSupplierQuote().get();
        given(supplierQuoteRepository.save(newSupplierQuote)).willReturn(savedSupplierQuote);

        SupplierQuote quote = supplierQuoteService.saveSupplierQuote(newSupplierQuote);

        assertThat(quote.getId()).isEqualTo(1L);
    }

    @Test
    void getSupplierQuoteById() {
        given(supplierQuoteRepository.findById(1L)).willReturn(getSavedSupplierQuote());

        Optional<SupplierQuote> quote = supplierQuoteService.getSupplierQuoteById(1L);

        assertThat(quote.isPresent()).isTrue();
        assertThat(quote.get().getSupplier().getSupplierName()).isEqualTo("Shelley Parts Ltd");
        assertThat(quote.get().getStockableProduct().getName()).isEqualTo("MF220");
    }

    @Test
    void getAllSupplierQuotesForSupplier() {
        given(supplierQuoteRepository.findBySupplier(shelleys)).willReturn(Arrays.asList(savedSupplierQuote,
                MF286Quote));

        List<SupplierQuote> quotes = supplierQuoteService.getAllSupplierQuotesForSupplier(shelleys);

        assertThat(quotes).isNotNull();
        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes).contains(MF286Quote);
        assertThat(quotes).contains(savedSupplierQuote);
    }

    @Test
    void getAllSupplierQuotesForStockableProduct() {
        given(supplierQuoteRepository.findByStockableProduct(MF220))
                .willReturn(Arrays.asList(savedSupplierQuote, FJIQuote));

        List<SupplierQuote> quotes = supplierQuoteService.getAllSupplierQuotesForStockableProduct(MF220);

        assertThat(quotes).isNotNull();
        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes).contains(savedSupplierQuote);
        assertThat(quotes).contains(FJIQuote);
    }

    @Test
    void getLastSupplierQuoteForStockableProductAndSupplier() {
        given(supplierQuoteRepository.findTopByStockableProductAndSupplierOrderByQuotationDateDesc(MF220, shelleys))
                .willReturn(savedSupplierQuote);

        SupplierQuote quote = supplierQuoteService.getLastSupplierQuoteForStockableProductAndSupplier(MF220, shelleys);
        assertThat(quote.getSupplier().getSupplierName()).isEqualTo("Shelley Parts Ltd");
        assertThat(quote.getStockableProduct().getName()).isEqualTo("MF220");
    }
}