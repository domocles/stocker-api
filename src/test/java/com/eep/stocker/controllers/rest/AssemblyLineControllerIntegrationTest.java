package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.assemblyline.*;
import com.eep.stocker.services.AssemblyLineService;
import com.eep.stocker.services.AssemblyService;
import com.eep.stocker.services.StockableProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssemblyLineControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AssemblyLineMapper mapper;

    private StockableProduct MF220;
    private StockableProduct MF286;
    private Assembly assembly1;
    private Assembly assembly1unsaved;
    private Assembly assembly2;

    private AssemblyLine assemblyLine1;
    private AssemblyLine assemblyLine2;
    private AssemblyLine assemblyLine3;

    @MockBean
    private AssemblyLineService assemblyLineService;

    @MockBean
    private AssemblyService assemblyService;

    @MockBean
    private StockableProductService stockableProductService;

    @BeforeEach
    public void setUp() throws Exception {
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

        assembly1unsaved = Assembly.builder()
                .name("Golf Decat")
                .mpn("EEP101")
                .category("Decat")
                .build();

        assembly1 = Assembly.builder()
                .name("Golf Decat")
                .mpn("EEP101")
                .category("Decat")
                .build();
        assembly1.setId(1L);

        assembly2 = Assembly.builder()
                .name("ST170 Mk2 Decat")
                .mpn("EEP103")
                .category("Decat")
                .build();
        assembly2.setId(2L);

        assemblyLine1 = AssemblyLine.builder().assembly(assembly1).stockableProduct(MF220).qty(3).build();
        assemblyLine1.setId(1L);

        assemblyLine2 = AssemblyLine.builder().assembly(assembly1).stockableProduct(MF220).qty(3).build();
        assemblyLine2.setId(2L);

        assemblyLine3 = AssemblyLine.builder().assembly(assembly2).stockableProduct(MF286).qty(3).build();
        assemblyLine3.setId(3L);
    }

    @Test
    void getAlLAssemblyLinesTest() {
        given(assemblyLineService.getAllAssemblyLines())
                .willReturn(Arrays.asList(assemblyLine1,assemblyLine2,assemblyLine3));

        var response = restTemplate.exchange(
                "/api/assembly-line/",
                HttpMethod.GET,
                null,
                GetAllAssemblyLinesResponse.class
        );

        var testRes1 = mapper.mapToLowDetailResponse(assemblyLine1);
        var testRes2 = mapper.mapToLowDetailResponse(assemblyLine2);
        var testRes3 = mapper.mapToLowDetailResponse(assemblyLine3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getAssemblyLines()).isNotNull().contains(testRes1, testRes2, testRes3)
        );
    }

    @Test
    void getAssemblyLineByIdTest() {
        given(assemblyLineService.getAssemblyLineByUid(anyString())).willReturn(Optional.of(assemblyLine1));

        var response = restTemplate.exchange(
                "/api/assembly-line/" + assemblyLine1.getUid().toString(),
                HttpMethod.GET,
                null,
                GetAssemblyLineResponse.class
        );

        var testResponse = mapper.mapToGetResponse(assemblyLine1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    public void getAssemblyLineByIdNotExistTest() {
        given(assemblyLineService.getAssemblyLineByUid(anyString())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly-line/" + assemblyLine1.getUid().toString(),
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(response.getBody()).isNotNull()
        );
    }

    @Test
    void saveAssemblyLineTest() {
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(assemblyService.getAssemblyByUid(anyString())).willReturn(Optional.of(assembly1));
        given(assemblyLineService.saveAssemblyLine(any(AssemblyLine.class)))
                .willReturn(Optional.of(assemblyLine1));

        var request = new CreateAssemblyLineRequest();
        request.setAssemblyId(assembly1.getUid().toString());
        request.setStockableProductId(MF220.getUid().toString());
        request.setQty(3.0);


        var response = restTemplate.exchange(
                "/api/assembly-line/",
                HttpMethod.POST,
                new HttpEntity<>(request),
                CreateAssemblyLineResponse.class
        );

        var testResponse = mapper.mapToCreateResponse(assemblyLine1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    void updateAssemblyLineTest() {
        given(assemblyLineService.getAssemblyLineByUid(anyString())).willReturn(Optional.of(assemblyLine3));
        given(assemblyService.getAssemblyByUid(anyString())).willReturn(Optional.of(assemblyLine2.getAssembly()));
        given(stockableProductService.getStockableProductByUid(anyString()))
                .willReturn(Optional.of(assemblyLine2.getStockableProduct()));
        given(assemblyLineService.saveAssemblyLine(any(AssemblyLine.class)))
                .willReturn(Optional.of(assemblyLine2));


        var request = new UpdateAssemblyLineRequest();
        request.setAssemblyId(assemblyLine2.getAssembly().getUid().toString());
        request.setStockableProductId(assemblyLine2.getStockableProduct().getUid().toString());
        request.setQty(assemblyLine2.getQty());

        var response = restTemplate.exchange(
                "/api/assembly-line/" + assemblyLine1.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateAssemblyLineResponse.class
        );

        var testResponse = mapper.mapToUpdateResponse(assemblyLine2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }


    @Test
    void updateAssemblyLineWithInvalidAssemblyUidTest() {
        given(assemblyLineService.getAssemblyLineByUid(anyString())).willReturn(Optional.of(assemblyLine3));
        given(assemblyService.getAssemblyByUid(anyString())).willReturn(Optional.of(assemblyLine2.getAssembly()));
        given(stockableProductService.getStockableProductByUid(anyString()))
                .willReturn(Optional.of(assemblyLine2.getStockableProduct()));
        given(assemblyLineService.saveAssemblyLine(any(AssemblyLine.class)))
                .willReturn(Optional.of(assemblyLine2));


        var request = new UpdateAssemblyLineRequest();
        request.setAssemblyId("invalid");
        request.setStockableProductId("invalid");
        request.setQty(assemblyLine2.getQty());

        var response = restTemplate.exchange(
                "/api/assembly-line/" + assemblyLine1.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ErrorResponse.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }

    @Test
    void deleteAssemblyLineByIdTest() {
        given(assemblyLineService.getAssemblyLineByUid(anyString())).willReturn(Optional.of(assemblyLine2));
        given(assemblyLineService.deleteAssemblyLineById(anyLong())).willReturn(Optional.of(assemblyLine2));

        var response = restTemplate.exchange(
                "/api/assembly-line/" + assemblyLine2.getUid().toString(),
                HttpMethod.DELETE,
                null,
                DeleteAssemblyLineResponse.class
        );

        var testResponse = mapper.mapToDeleteResponse(assemblyLine2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    public void deleteAssemblyLineByIdDoesNotExistTest() {
        given(assemblyLineService.deleteAssemblyLineById(anyLong())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly-line/delete/4",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }


    @Test
    void getAssemblyLinesForAssemblyTest() {
        given(assemblyService.getAssemblyByUid(anyString())).willReturn(Optional.of(assembly1));
        given(assemblyLineService.getAllAssemblyLinesForAssembly(assembly1))
                .willReturn(Arrays.asList(assemblyLine1, assemblyLine2, assemblyLine3));

        var response = restTemplate.exchange(
                "/api/assembly-line/assembly/" + assembly1.getUid().toString(),
                HttpMethod.GET,
                null,
               GetAllAssemblyLinesResponse.class
        );

        var testRes1 = mapper.mapToLowDetailResponse(assemblyLine1);
        var testRes2 = mapper.mapToLowDetailResponse(assemblyLine2);
        var testRes3 = mapper.mapToLowDetailResponse(assemblyLine3);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getAssemblyLines()).isNotNull().contains(testRes1, testRes2, testRes3)
        );
    }
}