package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import com.eep.stocker.dto.stockableproductnote.*;
import com.eep.stocker.services.StockableProductNoteService;
import com.eep.stocker.services.StockableProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockableProductNoteControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StockableProductNoteMapper noteMapper;

    @MockBean
    private StockableProductService productService;

    @MockBean
    private StockableProductNoteService noteService;

    private StockableProduct mf220;
    private StockableProduct mf236;

    private StockableProductNote mf220note1;
    private StockableProductNote mf220note2;
    private StockableProductNote mf220note3;
    private StockableProductNote mf236note1;

    @BeforeEach
    void setup() {
        mf220 = new StockableProduct();
        mf220.setId(1L);
        mf220.setMpn("EEP123");
        mf220.setCategory("Flange");
        mf220.setDescription("A flange");
        mf220.setInStock(25.);
        mf220.setStockPrice(1.26);
        mf220.setUnits("Flanges");
        mf220.setName("MF220");

        mf236 = new StockableProduct();
        mf236.setId(2L);
        mf236.setMpn("EEP456");
        mf236.setCategory("Flange");
        mf236.setDescription("A flange");
        mf236.setInStock(15.);
        mf236.setStockPrice(1.26);
        mf236.setUnits("Flanges");
        mf236.setName("MF236");

        mf220note1 = StockableProductNote.builder()
                .note("A flange with holes")
                .stockableProduct(mf220)
                .build();
        mf220note2 = StockableProductNote.builder()
                .note("Mild Steel")
                .stockableProduct(mf220)
                .build();
        mf220note3 = StockableProductNote.builder()
                .note("180mm x 90mm")
                .stockableProduct(mf220)
                .build();

        mf236note1 = StockableProductNote.builder()
                .note("57mm bore")
                .stockableProduct(mf236)
                .build();

        mf236note1 = StockableProductNote.builder()
                .note("10mm x 20mm oval holes")
                .stockableProduct(mf236)
                .build();
    }

    @Test
    void getStockableProductNoteByIdTest() {
        given(noteService.getByUid(anyString())).willReturn(Optional.of(mf220note2));

        var response = restTemplate.exchange("/api/stockable-product-note/" + mf220note2.getUid().toString(),
                HttpMethod.GET,
                null,
                GetStockableProductNoteResponse.class);

        var testResponse = noteMapper.mapToGetResponse(mf220note2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void getAllStockableProductNotesTest() {
        given(noteService.get()).willReturn(List.of(mf220note1, mf220note2, mf220note3, mf236note1));

        var response = restTemplate.exchange(
                "/api/stockable-product-note/",
                HttpMethod.GET,
                null,
                GetAllStockableProductNotesResponse.class
        );

        var testResult1 = noteMapper.mapToLowDetailResponse(mf220note1);
        var testResult2 = noteMapper.mapToLowDetailResponse(mf220note2);
        var testResult3 = noteMapper.mapToLowDetailResponse(mf220note3);
        var testResult4 = noteMapper.mapToLowDetailResponse(mf236note1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getProductNotes())
                        .contains(testResult1, testResult2, testResult3, testResult4)
        );
    }

    @Test
    void getAllStockableProductNotesForStockableProductTest() {
        given(noteService.getAllNotesForStockableProductUid(anyString()))
                .willReturn(List.of(mf220note1, mf220note2, mf220note3));

        var response = restTemplate.exchange(
                "/api/stockable-product-note/stockable-product/" + mf220.getUid().toString() + "/",
                HttpMethod.GET,
                null,
                GetAllStockableProductNotesByStockableProductResponse.class
        );

        var testResult1 = noteMapper.mapToLowDetailResponse(mf220note1);
        var testResult2 = noteMapper.mapToLowDetailResponse(mf220note2);
        var testResult3 = noteMapper.mapToLowDetailResponse(mf220note3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getProductNotes())
                        .contains(testResult1, testResult2, testResult3)
        );
    }

    @Test
    void saveStockableProductNoteTest() {
        var request = new CreateStockableProductNoteRequest();
        request.setStockableProductId(mf220note1.getStockableProduct().getUid().toString());
        request.setNote(mf220note1.getNote());

        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(mf220));
        given(noteService.saveNote(any(StockableProductNote.class))).willReturn(mf220note1);

        var response = restTemplate.postForEntity(
                "/api/stockable-product-note/",
                request,
                CreateStockableProductNoteResponse.class
        );

        var testResponse = noteMapper.mapToCreateResponse(mf220note1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void updateStockableProductNoteTest() {
        var request = new UpdateStockableProductNoteRequest();
        request.setStockableProductId(mf220.getUid().toString());
        request.setNote("An updated note");

        var updatedNote = mf220note1.toBuilder().note("An updated note").build();

        given(noteService.getByUid(anyString())).willReturn(Optional.of(mf220note1));
        given(noteService.saveNote(any(StockableProductNote.class))).willReturn(updatedNote);

        var response = restTemplate.exchange(
                "/api/stockable-product-note/" + mf220note1.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateStockableProductNoteResponse.class
        );

        var testResponse = noteMapper.mapToUpdateResponse(updatedNote);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }
}
