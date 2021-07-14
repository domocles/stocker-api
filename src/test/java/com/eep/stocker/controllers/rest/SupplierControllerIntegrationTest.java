package com.eep.stocker.controllers.rest;

import com.eep.stocker.dto.supplier.GetAllSuppliersResponse;
import com.eep.stocker.dto.supplier.GetSupplierResponse;
import com.eep.stocker.services.SupplierService;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierControllerIntegrationTest extends SupplierTestData {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SupplierService supplierService;

    @Test
    public void getSupplierByIdTest() {
        given(supplierService.getSupplierFromUid(anyString())).willReturn(Optional.of(supplier));

        ResponseEntity<GetSupplierResponse> res = restTemplate.exchange(
                "/api/suppliers/get/5",
                HttpMethod.GET,
                null,
                GetSupplierResponse.class);

        assertThat(res).isNotNull();
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull().isEqualTo(getSupplierResponse);
    }

    @Test
    public void getAllSuppliersTest() {
        given(supplierService.getAllSuppliers()).willReturn(Arrays.asList(supplier, shelleys, ukf, fji));

        ResponseEntity<GetAllSuppliersResponse> res = restTemplate.exchange(
                "/api/suppliers/get",
                HttpMethod.GET,
                null,
                GetAllSuppliersResponse.class
        );

        assertThat(res).isNotNull();
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().getAllSuppliers()).contains(getSupplierResponse);
    }
}
