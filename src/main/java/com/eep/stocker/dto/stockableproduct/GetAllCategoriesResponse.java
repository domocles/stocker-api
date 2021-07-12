package com.eep.stocker.dto.stockableproduct;

import lombok.Data;

import java.util.List;

@Data
public class GetAllCategoriesResponse {
    private List<String> allCategories;

    public void addCategory(String category) { this.allCategories.add(category); }
}
