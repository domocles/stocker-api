package com.eep.stocker.dto.deliveryline;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 13/09/2022
 * A standard response when a collection of delivery lines is required.
 */
@Data
public class GetListOfLowDetailDeliveryLineResponse {
    public final List<GetLowDetailDeliveryLineResponse> deliveryLines = new ArrayList<>();

    public void addDeliveryLine(GetLowDetailDeliveryLineResponse response) {
        this.deliveryLines.add(response);
    }
}
