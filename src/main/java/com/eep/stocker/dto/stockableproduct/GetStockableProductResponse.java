package com.eep.stocker.dto.stockableproduct;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetStockableProductResponse implements IStockableProductDTO.Id,
            IStockableProductDTO.Name, IStockableProductDTO.Mpn, IStockableProductDTO.Description,
            IStockableProductDTO.Category, IStockableProductDTO.Units, IStockableProductDTO.Tags,
            IStockableProductDTO.StockPrice, IStockableProductDTO.InStock, IStockableProductDTO.OnOrder {
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
