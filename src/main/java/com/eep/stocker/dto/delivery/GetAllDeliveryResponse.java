package com.eep.stocker.dto.delivery;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllDeliveryResponse {
    public List<GetDeliveryResponse> allDeliveries = new ArrayList<>();

    public void addDeliveryResponse(GetDeliveryResponse response) {
        this.allDeliveries.add(response);
    }
}
