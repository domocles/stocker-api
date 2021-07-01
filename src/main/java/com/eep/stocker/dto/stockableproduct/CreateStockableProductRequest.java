package com.eep.stocker.dto.stockableproduct;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CreateStockableProductRequest implements
        StockableProductDTO.Name,
        StockableProductDTO.Mpn,
        StockableProductDTO.Description,
        StockableProductDTO.Category,
        StockableProductDTO.Units,
        StockableProductDTO.Tags,
        StockableProductDTO.StockPrice,
        StockableProductDTO.InStock {
    private String name;
    private String mpn;
    private String description;
    private String category;
    private String units;
    private Set<String> tags;
    private BigDecimal stockPrice;
    private BigDecimal instock;
}
