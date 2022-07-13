package com.eep.stocker.dto.delivery;

import static com.eep.stocker.testdata.DeliveryTestData.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryMapperTest {
    @Autowired
    DeliveryMapper mapper;

    @Test
    void deliveryToDeliveryDTO() {
        var dto = mapper.deliveryToDeliveryDTO(delivery1);

        assertThat(dto).isEqualTo(deliveryDTO);
        assertThat(dto.getSupplier()).isEqualTo(supplierDTO);
    }

    @Test
    void deliveryToGetDeliveryResponse() {
        var dto = mapper.deliveryToGetDeliveryResponse(delivery1);

        assertThat(dto).isEqualTo(getDeliveryResponse);
        assertThat(dto.getSupplier()).isEqualTo(supplierDTO);
    }
}