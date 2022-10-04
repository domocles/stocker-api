package com.eep.stocker.dto.stockableproduct;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class UpdateStockableProductResponse implements
        IStockableProductDTO.Id,
        IStockableProductDTO.Name,
        IStockableProductDTO.Mpn,
        IStockableProductDTO.Description,
        IStockableProductDTO.Category,
        IStockableProductDTO.Units,
        IStockableProductDTO.Tags,
        IStockableProductDTO.StockPrice,
        IStockableProductDTO.InStock,
        IStockableProductDTO.OnOrder{
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
    private BigDecimal onOrder;
}
