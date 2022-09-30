package com.eep.stocker.dto.stockableproduct;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class CreateStockableProductRequest implements
        IStockableProductDTO.Name,
        IStockableProductDTO.Mpn,
        IStockableProductDTO.Description,
        IStockableProductDTO.Category,
        IStockableProductDTO.Units,
        IStockableProductDTO.Tags,
        IStockableProductDTO.StockPrice,
        IStockableProductDTO.InStock {
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "MPN must not be blank")
    private String mpn;
    private String description;
    @NotBlank(message = "Category must not be blank")
    private String category;
    private String units;
    @Singular
    private Set<String> tags;
    private BigDecimal stockPrice;
    private BigDecimal inStock;
}
