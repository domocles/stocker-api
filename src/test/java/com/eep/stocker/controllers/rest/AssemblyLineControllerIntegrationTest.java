package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.AssemblyLineService;
import com.eep.stocker.services.AssemblyService;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.apache.tomcat.jni.Error;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssemblyLineControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

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

    @Before
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

        assembly1unsaved = new Assembly(null, "Golf Decat", "EEP101", "Decat");
        assembly1 = new Assembly(1L, "Golf Decat", "EEP102", "Decat");
        assembly2 = new Assembly(2L, "ST170 Mk2 Decat", "EEP103", "Decat");

        assemblyLine1 = new AssemblyLine(1L, MF220, assembly1, 3);
        assemblyLine2 = new AssemblyLine(2L, MF220, assembly1, 3);
        assemblyLine3 = new AssemblyLine(3L, MF220, assembly2, 3);
    }

    @Test
    public void getAlLAssemblyLinesTest() {
        given(assemblyLineService.getAllAssemblyLines())
                .willReturn(Arrays.asList(assemblyLine1,assemblyLine2,assemblyLine3));

        ResponseEntity<List<AssemblyLine>> response = restTemplate.exchange(
                "/api/assembly-line/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AssemblyLine>>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().contains(assemblyLine1, assemblyLine2, assemblyLine3);
    }

    @Test
    public void getAssemblyLineByIdTest() {
        given(assemblyLineService.getAssemblyLineById(anyLong())).willReturn(Optional.of(assemblyLine1));

        ResponseEntity<AssemblyLine> response = restTemplate.exchange(
                "/api/assembly-line/get/4",
                HttpMethod.GET,
                null,
                AssemblyLine.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(assemblyLine1);
    }

    @Test
    public void getAssemblyLineByIdNotExistTest() {
        given(assemblyLineService.getAssemblyLineById(anyLong())).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly-line/get/4",
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void saveAssemblyLineTest() {
        given(assemblyLineService.saveAssemblyLine(any(AssemblyLine.class)))
                .willReturn(Optional.of(assemblyLine1));

        ResponseEntity<AssemblyLine> response = restTemplate.exchange(
                "/api/assembly-line/create",
                HttpMethod.POST,
                new HttpEntity<>(assemblyLine2),
                AssemblyLine.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assemblyLine1);
    }

    @Test
    public void updateAssemblyLineTest() {
        given(assemblyLineService.saveAssemblyLine(any(AssemblyLine.class)))
                .willReturn(Optional.of(assemblyLine2));

        ResponseEntity<AssemblyLine> response = restTemplate.exchange(
                "/api/assembly-line/update",
                HttpMethod.PUT,
                new HttpEntity<>(assembly1),
                AssemblyLine.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assemblyLine2);
    }

    @Test
    public void deleteAssemblyLineByIdTest() {
        given(assemblyLineService.deleteAssemblyLineById(anyLong())).willReturn(Optional.of(assemblyLine2));

        ResponseEntity<AssemblyLine> response = restTemplate.exchange(
                "/api/assembly-line/delete/4",
                HttpMethod.DELETE,
                null,
                AssemblyLine.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(assemblyLine2);
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
    public void deleteAssemblyLineTest() {
        given(assemblyLineService.deleteAssemblyLine(any(AssemblyLine.class)))
                .willReturn(Optional.of(assemblyLine2));

        ResponseEntity<AssemblyLine> response = restTemplate.exchange(
                "/api/assembly-line/delete",
                HttpMethod.DELETE,
                new HttpEntity<>(assemblyLine2),
                AssemblyLine.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(assemblyLine2);
    }

    @Test
    public void getAssemblyLinesForAssemblyTest() {
        given(assemblyService.getAssemblyById(anyLong())).willReturn(Optional.of(assembly1));
        given(assemblyLineService.getAllAssemblyLinesForAssembly(assembly1))
                .willReturn(Arrays.asList(assemblyLine1, assemblyLine2, assemblyLine3));

        ResponseEntity<List<AssemblyLine>> response = restTemplate.exchange(
                "/api/assembly-line/get/assembly/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AssemblyLine>>() {  }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().contains(assemblyLine1, assemblyLine2, assemblyLine3);
    }
}