package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.StockableProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockableProductMapperTest {

    @Autowired
    StockableProductMapper stockableProductMapper;

    @MockBean
    private StockableProductService service;

    private StockableProduct stockableProduct = StockableProduct.builder()
            .id(1L)
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(1.0)
            .inStock(50.0)
            .build();

    private CreateStockableProductRequest request = CreateStockableProductRequest.builder()
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(BigDecimal.valueOf(1))
            .inStock(BigDecimal.valueOf(50.0))
            .build();

    private CreateStockableProductResponse response = CreateStockableProductResponse.builder()
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(BigDecimal.valueOf(1))
            .inStock(BigDecimal.valueOf(50.0))
            .build();

    private GetStockableProductResponse getResponse = GetStockableProductResponse.builder()
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(BigDecimal.valueOf(1))
            .inStock(BigDecimal.valueOf(50.0))
            .build();

    @Test
    void stockableProductToStockableProductRequest() {

        CreateStockableProductRequest req = stockableProductMapper.mapToCreateRequest(stockableProduct);
        assertThat(req.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(req.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(req.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertThat(req.getCategory()).isEqualTo("Flex");
        assertThat(req.getTags()).contains("51mm");
        assertThat(req.getStockPrice().doubleValue()).isEqualTo(1.0);
        assertThat(req.getInStock().doubleValue()).isEqualTo(50.0);
    }

    @Test
    void stockableProductFromCreateStockableProductRequest() {
        StockableProduct stockableProduct = stockableProductMapper.mapFromCreateRequest(request);
        assertThat(stockableProduct.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(stockableProduct.getUid().toString()).isNotEmpty();
        assertThat(stockableProduct.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(stockableProduct.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertThat(stockableProduct.getCategory()).isEqualTo("Flex");
        assertThat(stockableProduct.getTags()).contains("51mm");
        assertThat(stockableProduct.getStockPrice()).isEqualTo(1.0);
        assertThat(stockableProduct.getInStock()).isEqualTo(50.0);
    }

    @Test
    void createStockableProductResponseFromStockableProduct() {
        var res = stockableProductMapper.mapToCreateResponse(stockableProduct);
        assertThat(res.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(res.getId()).isNotEmpty();
        assertThat(res.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(res.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertThat(res.getCategory()).isEqualTo("Flex");
        assertThat(res.getTags()).contains("51mm");
        assertThat(res.getStockPrice()).isEqualTo(BigDecimal.valueOf(1.0));
        assertThat(res.getInStock()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    void createGetStockableProductResponseFromStockableProduct() {
        var res = stockableProductMapper.mapToGetResponse(stockableProduct, 0.0);
        assertThat(res.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(res.getId()).isNotEmpty();
        assertThat(res.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(res.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertAll(
                () -> assertThat(res.getCategory()).isEqualTo("Flex"),
                () -> assertThat(res.getTags()).contains("51mm"),
                () -> assertThat(res.getStockPrice()).isEqualTo(BigDecimal.valueOf(1.0)),
                () -> assertThat(res.getInStock()).isEqualTo(BigDecimal.valueOf(50.0)),
                () -> assertThat(res.getOnOrder()).isEqualTo(BigDecimal.valueOf(100.0))
        );
    }
}