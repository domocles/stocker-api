package com.eep.stocker.dto.purchaseorder;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllPurchaseOrdersResponse {
    public final List<GetPurchaseOrderResponse> allPurchaseOrders = new ArrayList<>();

    public void addPurchaseOrder(GetPurchaseOrderResponse response) { this.allPurchaseOrders.add(response); }
}
