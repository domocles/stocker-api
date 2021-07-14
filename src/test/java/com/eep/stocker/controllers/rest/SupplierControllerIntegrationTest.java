package com.eep.stocker.controllers.rest;

import com.eep.stocker.dto.supplier.GetSupplierResponse;
import com.eep.stocker.services.SupplierService;
import com.eep.stocker.testdata.SupplierTestData;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
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

    /*@Test
    public void getAllSuppliersTest() {
        given(supplierService.getAllSuppliers()).willReturn(Arrays.asList(shelleys));
    }*/
}
