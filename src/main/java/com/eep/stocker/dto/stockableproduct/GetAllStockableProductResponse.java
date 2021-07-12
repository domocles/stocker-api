package com.eep.stocker.dto.stockableproduct;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllStockableProductResponse {
    private List<GetStockableProductResponse> allStockableProduct = new ArrayList<>();

    public void addGetStockableProductResponse(GetStockableProductResponse stockableProductResponse) {
        this.allStockableProduct.add(stockableProductResponse);
    }
}
