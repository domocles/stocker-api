package com.eep.stocker.services;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.repository.ISupplierQuoteRepository;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class SupplierQuoteServiceTest extends SupplierTestData {

    @Mock
    private ISupplierQuoteRepository supplierQuoteRepository;

    private SupplierQuoteService supplierQuoteService;
    private SupplierQuote newSupplierQuote;
    private SupplierQuote savedSupplierQuote;
    private SupplierQuote MF286Quote;
    private SupplierQuote FJIQuote;
    private StockableProduct MF220;
    private StockableProduct MF286;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.supplierQuoteService = new SupplierQuoteService(supplierQuoteRepository);
        newSupplierQuote = getNewSupplierQuote().get();
        savedSupplierQuote = getSavedSupplierQuote().get();


        MF220 = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        MF286 = StockableProduct.builder()
                .id(2L)
                .name("MF286")
                .mpn("EEP200919002")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.45)
                .inStock(75.0)
                .build();

        MF286Quote = new SupplierQuote(MF286, shelleys, new Date(), 15.0, 2.76);
        FJIQuote = new SupplierQuote(MF220, fji, new Date(), 100.0, 1.96);
    }

    private static Optional<SupplierQuote> getNewSupplierQuote() {
        Supplier supplier = Supplier.builder()
                .supplierName("Shelley Parts Ltd")
                .defaultCurrency("GBP")
                .emailAddress("jon.horton@shelleyparts.co.uk")
                .telephoneNumber("01527 584285")
                .build();

        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units(("Flanges"))
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, new Date(), 15.0, 1.72D);
        return Optional.of(quote);
    }

    private static Optional<SupplierQuote> getSavedSupplierQuote() {
        Supplier supplier = Supplier.builder()
                .supplierName("Shelley Parts Ltd")
                .defaultCurrency("GBP")
                .emailAddress("jon.horton@shelleyparts.co.uk")
                .telephoneNumber("01527 584285")
                .build();

        StockableProduct stockableProduct = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, new Date(), 15.0, 1.72D);
        quote.setId(1L);
        return Optional.of(quote);
    }

    @Test
    void getAllSupplierQuotesTest() {
        given(supplierQuoteRepository.findAll()).willReturn(Arrays.asList(MF286Quote, FJIQuote));

        List<SupplierQuote> quotes = supplierQuoteService.getAllSupplierQuotes();

        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes.contains(MF286Quote)).isTrue();
        assertThat(quotes.contains(FJIQuote)).isTrue();
    }

    @Test
    void saveSupplierQuoteTest() {
        assertThat(supplierQuoteService).isNotNull();

        SupplierQuote newSupplierQuote = getNewSupplierQuote().get();
        SupplierQuote savedSupplierQuote = getSavedSupplierQuote().get();
        given(supplierQuoteRepository.save(newSupplierQuote)).willReturn(savedSupplierQuote);

        SupplierQuote quote = supplierQuoteService.saveSupplierQuote(newSupplierQuote);

        assertThat(quote.getId()).isEqualTo(1L);
    }

    @Test
    void getSupplierQuoteByIdTest() {
        given(supplierQuoteRepository.findById(1L)).willReturn(getSavedSupplierQuote());

        Optional<SupplierQuote> quote = supplierQuoteService.getSupplierQuoteById(1L);

        assertThat(quote.isPresent()).isTrue();
        assertThat(quote.get().getSupplier().getSupplierName()).isEqualTo("Shelley Parts Ltd");
        assertThat(quote.get().getStockableProduct().getName()).isEqualTo("MF220");
    }

    @Test
    void getAllSupplierQuotesForSupplierTest() {
        given(supplierQuoteRepository.findBySupplier(shelleys)).willReturn(Arrays.asList(savedSupplierQuote,
                MF286Quote));

        List<SupplierQuote> quotes = supplierQuoteService.getAllSupplierQuotesForSupplier(shelleys);

        assertThat(quotes).isNotNull();
        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes).contains(MF286Quote);
        assertThat(quotes).contains(savedSupplierQuote);
    }

    @Test
    void getAllSupplierQuotesForStockableProductTest() {
        given(supplierQuoteRepository.findByStockableProduct(MF220))
                .willReturn(Arrays.asList(savedSupplierQuote, FJIQuote));

        List<SupplierQuote> quotes = supplierQuoteService.getAllSupplierQuotesForStockableProduct(MF220);

        assertThat(quotes).isNotNull();
        assertThat(quotes.size()).isEqualTo(2);
        assertThat(quotes).contains(savedSupplierQuote);
        assertThat(quotes).contains(FJIQuote);
    }

    @Test
    void getLastSupplierQuoteForStockableProductAndSupplierTest() {
        given(supplierQuoteRepository.findTopByStockableProductAndSupplierOrderByQuotationDateDesc(MF220, shelleys))
                .willReturn(savedSupplierQuote);

        SupplierQuote quote = supplierQuoteService.getLastSupplierQuoteForStockableProductAndSupplier(MF220, shelleys);
        assertThat(quote.getSupplier().getSupplierName()).isEqualTo("Shelley Parts Ltd");
        assertThat(quote.getStockableProduct().getName()).isEqualTo("MF220");
    }

    @Test
    void updateSupplierQuoteTest() {
        given(supplierQuoteRepository.save(any(SupplierQuote.class))).willReturn(savedSupplierQuote);

        SupplierQuote quote = supplierQuoteService.updateSupplierQuote(savedSupplierQuote);

        assertThat(quote).isEqualTo(savedSupplierQuote);
        assertThat(quote.getId()).isNotNull();

    }
}