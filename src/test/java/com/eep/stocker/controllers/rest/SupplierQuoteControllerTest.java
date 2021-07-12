package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.exceptions.SupplierDoesNotExistException;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SupplierQuoteController.class)
public class SupplierQuoteControllerTest {

    @MockBean
    private SupplierQuoteService supplierQuoteService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private StockableProductService stockableProductService;

    @Autowired
    private MockMvc mockMvc;

    private SupplierQuote getSupplierQuote() {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

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
        return quote;
    }

    @Test
    public void getAllSuppliersTest() throws Exception {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

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
        given(supplierQuoteService.getAllSupplierQuotes())
                .willReturn(Arrays.asList(quote));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/supplier-quote/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplier.supplierName").value("Shelley Parts Ltd"));
    }

    @Test
    public void getSupplierQuoteByIdTest() throws Exception {

        given(supplierQuoteService.getSupplierQuoteById(anyLong()))
                .willReturn(java.util.Optional.of(getSupplierQuote()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/supplier-quote/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("supplier.supplierName").value("Shelley Parts Ltd"));
    }

    @Test
    public void getSupplierQuotesForNonExistantSuppliersTest() throws Exception {
        given(supplierQuoteService.getAllSupplierQuotesForSupplier(any(Supplier.class)))
                .willThrow(new SupplierDoesNotExistException("Supplier Does Not Exist!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/supplier-quote/supplier/get/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSupplierQuotesForSupplierTest() throws Exception {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

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

        given(supplierQuoteService.getAllSupplierQuotesForSupplier(any(Supplier.class)))
                .willReturn(Arrays.asList(quote));
        given(supplierService.getSupplierFromId(anyLong())).willReturn(java.util.Optional.of(supplier));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/supplier-quote/supplier/get/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockableProduct.name").value("MF220"))
                .andExpect(jsonPath("$[0].supplier.supplierName").value("Shelley Parts Ltd"));
    }

    @Test
    public void getSupplierQuotesForStockableProductTest() throws Exception {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

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

        given(stockableProductService.getStockableProductByID(anyLong()))
                .willReturn(java.util.Optional.of(stockableProduct));

        given(supplierQuoteService.getAllSupplierQuotesForStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(quote));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/supplier-quote/stockable-product/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockableProduct.name").value("MF220"))
                .andExpect(jsonPath("$[0].supplier.supplierName").value("Shelley Parts Ltd"));
    }

    @Test
    public void updateSupplierQuoteTest() throws Exception {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

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

        given(supplierQuoteService.updateSupplierQuote(any(SupplierQuote.class))).willReturn(quote);
    }

    @Test
    public void createSupplierQuoteTest() throws Exception {
        Supplier supplier = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

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
        SupplierQuote returnedQuote = new SupplierQuote(stockableProduct, quote.getSupplier(), quote.getQuotationDate(), 15.0, 1.72D);
        returnedQuote.setId(1L);


        given(supplierQuoteService.saveSupplierQuote(any(SupplierQuote.class))).willReturn(returnedQuote);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/supplier-quote/create")
                .content(asJsonString(quote))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("stockableProduct.name").value("MF220"))
                .andExpect(jsonPath("id").value(1L))
                .andReturn();
        String res = result.getResponse().getContentAsString();
        SupplierQuote retval = fromJsonString(res, SupplierQuote.class);

        assertThat(retval).isEqualTo(returnedQuote);
    }

    public static String asJsonString(final Object obj) {
        try {
            String test = new ObjectMapper().writeValueAsString(obj);
            return test;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonString(String json, Class<T> type) {
        try {
            T obj = new ObjectMapper().readValue(json, type);
            return obj;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
