package com.eep.stocker.controllers.rest;

import com.eep.stocker.controllers.error.ErrorResponse;
import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.services.AssemblyService;
import com.eep.stocker.services.StockableProductService;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssemblyControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

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

    @Before
    public void setUp() {
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

        assembly1unsaved = new Assembly(null, "Golf Decat", "EEP101", "Decat");
        assembly1 = new Assembly(1L, "Golf Decat", "EEP102", "Decat");
        assembly2 = new Assembly(2L, "ST170 Mk2 Decat", "EEP103", "Decat");

        assemblyLine1 = new AssemblyLine(1L, MF220, assembly1, 3);
        assemblyLine2 = new AssemblyLine(2L, MF220, assembly1, 3);
        assemblyLine3 = new AssemblyLine(3L, MF220, assembly2, 3);
    }

    @Test
    public void getAllAssembliesTest() {
        given(assemblyService.getAllAssemblies()).willReturn(Arrays.asList(assembly1, assembly2));

        ResponseEntity<List<Assembly>> response = restTemplate.exchange(
                "/api/assembly/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Assembly>>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains(assembly1, assembly2);
    }

    @Test
    public void getAssemblyByIdTest() {
        Optional<Assembly> assyReturn = Optional.of(assembly1);
        given(assemblyService.getAssemblyById(anyLong())).willReturn(assyReturn);

        ResponseEntity<Assembly> response = restTemplate.exchange(
                "/api/assembly/get/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Assembly>() {  }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assembly1);
    }

    @Test
    public void saveAssemblyTest() {
        given(assemblyService.saveAssembly(assembly1unsaved)).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.postForEntity(
                "/api/assembly/create",
                assembly1unsaved,
                Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assembly1);
    }

    @Test
    public void saveAssemblyMpnAlreadyExistsTest() {
        given(assemblyService.saveAssembly(any(Assembly.class))).willThrow(new MpnNotUniqueException("Assembly with mpn of EEP101 already exists"));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/assembly/create",
                assembly1unsaved,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetails()).isNotNull().contains("Assembly with mpn of EEP101 already exists");
    }

    @Test
    public void saveNullAssemblyTest() {
        given(assemblyService.saveAssembly(assembly1unsaved)).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.postForEntity(
                "/api/assembly/create",
                null,
                Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void updateAssemblyTest() {
        given(assemblyService.updateAssembly(any(Assembly.class))).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.exchange(
          "/api/assembly/update",
          HttpMethod.PUT,
          new HttpEntity<>(assembly2), Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assembly1);
    }

    @Test
    public void deleteAssemblyByIdTest() {
        given(assemblyService.deleteAssemblyById(anyLong())).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.exchange(
                "/api/assembly/delete/5",
                HttpMethod.DELETE,
                null, Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assembly1);
    }

    @Test
    public void deleteAssemblyTest() {
        given(assemblyService.deleteAssemblyById(anyLong())).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.exchange(
                "/api/assembly/delete/5",
                HttpMethod.DELETE,
               new HttpEntity<>(assembly1), Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(assembly1);
    }

    @Test
    public void getAllAssembliesForComponentTest() {
        given(stockableProductService.getStockableProductByID(anyLong())).willReturn(Optional.of(MF220));
        given(assemblyService.getAllAssembliesByComponent(any(StockableProduct.class)))
                .willReturn(new HashSet<>(Arrays.asList(assembly1, assembly2)));

        ResponseEntity<List<Assembly>> response = restTemplate.exchange(
                "/api/assembly/component/4",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Assembly>>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().contains(assembly1, assembly2);
    }

    @Test
    public void getAllAssembliesForComponentThatDoesntExistTest() {
        given(stockableProductService.getStockableProductByID(anyLong())).willReturn(Optional.empty());
        given(assemblyService.getAllAssembliesByComponent(any(StockableProduct.class)))
                .willReturn(new HashSet<>(Arrays.asList(assembly1, assembly2)));

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly/component/4",
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        //assertThat(response.getBody()).isNotNull().contains(assembly1, assembly2);
    }

    @Test
    public void getAssemblyByMpnTest() {
        given(assemblyService.getAssemblyByMpn(any(String.class))).willReturn(Optional.of(assembly1));

        ResponseEntity<Assembly> response = restTemplate.exchange(
                "/api/assembly/mpn/EEP101",
                HttpMethod.GET,
                null,
                Assembly.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(assembly1);
    }

    @Test
    public void getAssemblyByMpnNotExistTest() {
        given(assemblyService.getAssemblyByMpn(any(String.class))).willReturn(Optional.empty());

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/assembly/mpn/EEP101",
                HttpMethod.GET,
                null,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetails()).isNotNull();
        assertThat(response.getBody().getDetails().get(0)).isEqualTo("Assembly with mpn of EEP101 does not exist");
    }
}