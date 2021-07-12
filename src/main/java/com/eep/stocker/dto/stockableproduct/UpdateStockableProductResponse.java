package com.eep.stocker.dto.stockableproduct;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class UpdateStockableProductResponse implements
        StockableProductDTO.Id,
        StockableProductDTO.Name,
        StockableProductDTO.Mpn,
        StockableProductDTO.Description,
        StockableProductDTO.Category,
        StockableProductDTO.Units,
        StockableProductDTO.Tags,
        StockableProductDTO.StockPrice,
        StockableProductDTO.InStock {
    private String id;
    private String name;
    private String mpn;
    private String description;
    private String category;
    private String units;
    @Singular
    private Set<String> tags;
    private BigDecimal stockPrice;
    private BigDecimal inStock;
}
