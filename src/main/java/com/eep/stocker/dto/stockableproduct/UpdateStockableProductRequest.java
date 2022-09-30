package com.eep.stocker.dto.stockableproduct;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

/***
 * @author Sam Burns
 * @version 1.0
 * 29/09/2022
 *
 * Request DTO for the update stockable product endpoint
 */
@Data
@Builder
public class UpdateStockableProductRequest implements
        IStockableProductDTO.Name,
        IStockableProductDTO.Mpn,
        IStockableProductDTO.Description,
        IStockableProductDTO.Category,
        IStockableProductDTO.Units,
        IStockableProductDTO.Tags,
        IStockableProductDTO.StockPrice,
        IStockableProductDTO.InStock {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "MPN cannot be blank")
    private String mpn;
    private String description;
    @NotBlank(message = "Category cannot be blank")
    private String category;
    private String units;
    @Singular
    private Set<String> tags;
    private BigDecimal stockPrice;
    private BigDecimal inStock;
}
