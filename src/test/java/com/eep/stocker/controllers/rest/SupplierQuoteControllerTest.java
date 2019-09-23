package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.services.SupplierQuoteService;
import com.eep.stocker.services.SupplierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashSet;

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

        StockableProduct stockableProduct = new StockableProduct(1L, "MF220",
                "EEP200919001",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.72D,
                25.0D);

        SupplierQuote quote = new SupplierQuote(stockableProduct, supplier, new Date(), 15.0, 1.72D);
        return quote;
    }

    @Test
    public void getSupplierQuoteByIdTest() throws Exception {
        given(supplierQuoteService.getSupplierQuoteById(anyLong()))
                .willReturn(java.util.Optional.of(getSupplierQuote()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/supplier-quote/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("supplier.supplierName").value("Shelley Parts Ltd"));
    }
}
