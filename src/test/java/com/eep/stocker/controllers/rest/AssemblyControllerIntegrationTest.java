package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.assembly.*;
import com.eep.stocker.services.AssemblyService;
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
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssemblyControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AssemblyMapper assemblyMapper;

    @MockBean
    private AssemblyService assemblyService;

    @MockBean
    private StockableProductService stockableProductService;

    private StockableProduct MF220;
    private StockableProduct MF286;
    private Assembly assembly1;
    private Assembly assembly1unsaved;
    private Assembly assembly2;

    private AssemblyLine assemblyLine1;
    private AssemblyLine assemblyLine2;
    private AssemblyLine assemblyLine3;

    @BeforeEach
    public void setUp() {
        MF220 = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        MF286 = StockableProduct.builder()
                .id(2L)
                .name("MF286")
                .mpn("EEP200919002")
                .description("Mild Steel Flange")
                .category("Flanges")
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

        assemblyLine3 = AssemblyLine.builder().assembly(assembly2).stockableProduct(MF220).qty(3).build();
        assemblyLine3.setId(3L);
    }

    @Test
    void addSubAssemblyTest() {
        given(assemblyService.getAssemblyByUid(assembly1.getUid().toString())).willReturn(Optional.of(assembly1));
        given(assemblyService.getAssemblyByUid(assembly2.getUid().toString())).willReturn(Optional.of(assembly2));

        var testAssy = assembly1.toBuilder().subAssembly(assembly2).build();

        given(assemblyService.addSubAssemblyToAssemblyById(anyLong(),anyLong())).willReturn(Optional.of(testAssy));

        var response = restTemplate.exchange(
                "/api/assembly/addsubassembly/" + assembly1.getUid().toString() + "/" + assembly2.getUid().toString(),
                HttpMethod.PUT,
                null,
                UpdateAssemblyResponse.class
        );

        var testResponse = assemblyMapper.mapToUpdateResponse(testAssy);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    void getAllAssembliesTest() {
        given(assemblyService.getAllAssemblies()).willReturn(Arrays.asList(assembly1, assembly2));

        var response = restTemplate.exchange(
                "/api/assembly/",
                HttpMethod.GET,
                null,
                GetAllAssembliesResponse.class
        );

        var testResponse1 = assemblyMapper.mapToGetLowDetailResponse(assembly1);
        var testResponse2 = assemblyMapper.mapToGetLowDetailResponse(assembly2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAssemblies()).contains(testResponse1, testResponse2)
        );
    }

    @Test
    void getAssemblyByUidTest() {
        Optional<Assembly> assyReturn = Optional.of(assembly1);
        given(assemblyService.getAssemblyByUid(anyString())).willReturn(assyReturn);

        var response = restTemplate.exchange(
                "/api/assembly/" + assembly1.getUid().toString() ,
                HttpMethod.GET,
                null,
                GetAssemblyResponse.class
        );

        var testResponse = assemblyMapper.mapToGetResponse(assembly1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void saveAssemblyTest() {
        given(assemblyService.saveAssembly(any(Assembly.class))).willReturn(Optional.of(assembly1));

        var request = new CreateAssemblyRequest();
        request.setName(assembly1.getName());
        request.setMpn(assembly1.getMpn());
        request.setDescription(assembly1.getDescription());
        request.setCategory(assembly1.getCategory());
        request.getTags().addAll(assembly1.getTags());

        var response = restTemplate.postForEntity(
                "/api/assembly/",
                request,
                CreateAssemblyResponse.class
        );

        var testResponse = assemblyMapper.mapTpCreateResponse(assembly1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    void saveAssemblyMpnAlreadyExistsTest() {
        given(assemblyService.saveAssembly(any(Assembly.class))).willThrow(new MpnNotUniqueException("Assembly with mpn of EEP101 already exists"));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/assembly/",
                assembly1unsaved,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails()).isNotNull().contains("Assembly with mpn of EEP101 already exists");
    }

    @Test
    void saveNullAssemblyTest() {
        given(assemblyService.saveAssembly(assembly1unsaved)).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.postForEntity(
                "/api/assembly/",
                null,
                Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void updateAssemblyTest() {
        given(assemblyService.getAssemblyByUid(anyString())).willReturn(Optional.of(assembly1));
        given(assemblyService.saveAssembly(any(Assembly.class))).willReturn(Optional.of(assembly2));

        var request = new UpdateAssemblyRequest();
        request.setName(assembly2.getName());
        request.setDescription(assembly2.getDescription());
        request.setMpn(assembly2.getMpn());
        request.setCategory(assembly2.getCategory());
        request.getTags().addAll(assembly2.getTags());

        var response = restTemplate.exchange(
            "/api/assembly/" + assembly1.getUid(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateAssemblyResponse.class
        );

        var testResponse = assemblyMapper.mapToUpdateResponse(assembly2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody()).isEqualTo(testResponse)
        );
    }

    @Test
    void deleteAssemblyByIdTest() {
        given(assemblyService.deleteAssemblyByUid(anyString())).willReturn(Optional.of(assembly1));

        var response = restTemplate.exchange(
                "/api/assembly/" + assembly1.getUid().toString(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllAssembliesForComponentTest() {
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(assemblyService.getAllAssembliesByComponent(any(StockableProduct.class)))
                .willReturn(new HashSet<>(Arrays.asList(assembly1, assembly2)));

        var response = restTemplate.exchange(
                "/api/assembly/component/" + MF220.getUid().toString(),
                HttpMethod.GET,
                null,
                GetAssembliesByComponentResponse.class
        );

        var testResponse1 = assemblyMapper.mapToGetLowDetailResponse(assembly1);
        var testResponse2 = assemblyMapper.mapToGetLowDetailResponse(assembly2);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getAssemblies()).isNotNull().contains(testResponse1, testResponse2)
        );
    }

    @Test
    void getAllAssembliesForComponentThatDoesntExistTest() {
        given(stockableProductService.getStockableProductByUid(anyString())).willReturn(Optional.empty());
        given(assemblyService.getAllAssembliesByComponent(any(StockableProduct.class)))
                .willReturn(new HashSet<>(Arrays.asList(assembly1, assembly2)));

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly/component/" + UUID.randomUUID(),
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        //assertThat(response.getBody()).isNotNull().contains(assembly1, assembly2);
    }

    @Test
    void getAssemblyByMpnTest() {
        given(assemblyService.getAssemblyByMpn(any(String.class))).willReturn(Optional.of(assembly1));

        var response = restTemplate.exchange(
                "/api/assembly/mpn/EEP101",
                HttpMethod.GET,
                null,
                GetAssemblyByMpnResponse.class
        );

        var testResponse = assemblyMapper.mapToGetByMpnResponse(assembly1);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    void getAssemblyByMpnNotExistTest() {
        given(assemblyService.getAssemblyByMpn(any(String.class))).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly/mpn/EEP101",
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getDetails()).isNotNull(),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getDetails().get(0)).isEqualTo("Assembly with mpn of EEP101 does not exist")
        );
    }
}