package com.eep.stocker.dto.purchaseorderline;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/09/2022
 * A standard response for the purchase order line that only returns the id of domain objects.  Used when a lot of
 * purchase order lines are going to be returned.
 */
@Data
public class GetListOfLowDetailResponse {
    public final List<GetPurchaseOrderLineLowDetailResponse> allPurchaseOrderLines = new ArrayList<>();

    public void addPurchaseOrderLine(GetPurchaseOrderLineLowDetailResponse response) {
        this.allPurchaseOrderLines.add(response);
    }
}
