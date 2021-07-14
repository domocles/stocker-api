package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import com.eep.stocker.dto.stockableproduct.UpdateStockableProductRequest;
import com.eep.stocker.dto.stockableproduct.UpdateStockableProductResponse;
import com.eep.stocker.services.StockableProductNoteService;
import com.eep.stocker.services.StockableProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StockableProductService productService;

    @MockBean
    private StockableProductNoteService stockableProductNoteService;

    private StockableProductMapper stockableProductMapper = Mappers.getMapper(StockableProductMapper.class);

    private StockableProduct MF220;
    private StockableProduct MF286;

    private UpdateStockableProductRequest updateStockableProductRequest;
    private UpdateStockableProductResponse updateStockableProductResponse;

    @BeforeEach
    public void setUp() {
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

        updateStockableProductRequest = UpdateStockableProductRequest.builder()
                .id(MF220.getUid().toString())
                .name("MF220")
                .mpn("EEP200919005")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(BigDecimal.valueOf(1.72))
                .inStock(BigDecimal.valueOf(25.0))
                .build();

        updateStockableProductResponse = UpdateStockableProductResponse.builder()
                .id(MF220.getUid().toString())
                .name("MF220")
                .mpn("EEP200919005")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(BigDecimal.valueOf(1.72))
                .inStock(BigDecimal.valueOf(25.0))
                .build();
    }

    @Test
    public void updateStockableProductTest() {
        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(productService.findStockableProductByMpn(anyString())).willReturn(Optional.empty());
        given(productService.updateStockableProduct(any(StockableProduct.class))).willReturn(MF220);

        UpdateStockableProductRequest req = updateStockableProductRequest;

        ResponseEntity<UpdateStockableProductResponse> res = restTemplate.exchange(
                "/api/stockable-products/update",
                HttpMethod.PUT,
                new HttpEntity<>(updateStockableProductRequest),
                UpdateStockableProductResponse.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody()).isEqualTo(updateStockableProductResponse);
    }

    @Test
    public void updateMpnWithClashThrowsMpnAlreadyExistsTest() {
        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(productService.findStockableProductByMpn(anyString())).willReturn(Optional.of(MF286));

        UpdateStockableProductRequest req = updateStockableProductRequest;

        ResponseEntity<UpdateStockableProductResponse> res = restTemplate.exchange(
                "/api/stockable-products/update",
                HttpMethod.PUT,
                new HttpEntity<>(updateStockableProductRequest),
                UpdateStockableProductResponse.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
