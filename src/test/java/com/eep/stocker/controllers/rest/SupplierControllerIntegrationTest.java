package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.supplier.*;
import com.eep.stocker.services.SupplierService;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierControllerIntegrationTest extends SupplierTestData {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SupplierService supplierService;

    @Test
    void getSupplierByIdTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));

        ResponseEntity<GetSupplierResponse> res = restTemplate.exchange(
                "/api/suppliers/" + supplier.getUid().toString(),
                HttpMethod.GET,
                null,
                GetSupplierResponse.class);

        assertAll(
                () -> assertThat(res).isNotNull(),
                () -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(res.getBody()).isNotNull().isEqualTo(getSupplierResponse)
        );
    }

    @Test
    void getSupplierByNonExistantIdTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.empty());

        ResponseEntity<GetSupplierResponse> res = restTemplate.exchange(
                "/api/suppliers/get/" + supplier.getUid().toString(),
                HttpMethod.GET,
                null,
                GetSupplierResponse.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllSuppliersTest() {
        given(supplierService.getAllSuppliers()).willReturn(Arrays.asList(supplier, shelleys, ukf, fji));

        ResponseEntity<GetAllSuppliersResponse> res = restTemplate.exchange(
                "/api/suppliers/",
                HttpMethod.GET,
                null,
                GetAllSuppliersResponse.class
        );

        assertAll(
                () -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(res.getBody().getAllSuppliers()).isNotNull().contains(getSupplierResponse)
        );
    }

    @Test
    void createSupplierTest() {
        given(supplierService.saveSupplier(any(Supplier.class))).willReturn(supplier);

        CreateSupplierRequest request = createSupplierRequest;

        ResponseEntity<CreateSupplierResponse> response = restTemplate.exchange(
          "/api/suppliers/create",
                HttpMethod.POST,
                new HttpEntity<>(request),
                CreateSupplierResponse.class

        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(createSupplierResponse);
    }

    @Test
    void createSupplierWhichAlreadyExistsCausesError() {
        given(supplierService.supplierExists(anyString())).willReturn(true);

        CreateSupplierRequest request = createSupplierRequest;

        ResponseEntity<CreateSupplierResponse> response = restTemplate.exchange(
                "/api/suppliers/create",
                HttpMethod.POST,
                new HttpEntity<>(request),
                CreateSupplierResponse.class

        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateSupplierTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));
        given(supplierService.saveSupplier(any(Supplier.class))).willReturn(supplier);

        var request = updateSupplierRequest;

        var response = restTemplate.exchange(
                "/api/suppliers/" + supplier.getUid().toString() ,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateSupplierResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateNonExistantSupplierCreatesSupplierWithExistingNameReturnsConflict() {
        given(supplierService.supplierExists(anyString())).willReturn(true);

        var request = updateSupplierRequestNoId;

        var response = restTemplate.exchange(
                "/api/suppliers/" + ukf.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateSupplierResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateExistingSupplierWithExistingNameReturnsConflict() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(ukf));
        given(supplierService.supplierExists(anyString())).willReturn(true);

        var request = updateSupplierRequest;
        request.setSupplierName("shelleys");

        var response = restTemplate.exchange(
                "/api/suppliers/" + ukf.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                UpdateSupplierResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void deleteSupplierTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));
        given(supplierService.deleteSupplierById(anyLong())).willReturn(Optional.of(supplier));

        var response = restTemplate.exchange(
                "/api/suppliers/" + supplier.getUid().toString(),
                HttpMethod.DELETE,
                null,
                DeletedSupplierResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(deletedSupplierResponse);
    }

    @Test
    void deleteSupplierDoesNotExistReturnsNotFoundTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.empty());

        var response = restTemplate.exchange(
                "/api/suppliers/delete/1",
                HttpMethod.DELETE,
                null,
                DeletedSupplierResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
