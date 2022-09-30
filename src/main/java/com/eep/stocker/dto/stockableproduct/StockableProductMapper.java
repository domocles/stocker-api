package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface StockableProductMapper {

    CreateStockableProductRequest stockableProductToStockableProductRequest(StockableProduct stockableProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    StockableProduct stockableProductFromCreateStockableProductRequest(CreateStockableProductRequest request);

    @Mapping(target = "id", source = "uid")
    CreateStockableProductResponse createStockableProductResponseFromStockableProduct(StockableProduct product);

    @Mapping(target = "id", source = "uid")
    GetStockableProductResponse stockableProductResponseFromStockableProduct(StockableProduct product);

    @Mapping(target = "id", source = "uid")
    UpdateStockableProductResponse updateStockableResponseFromStockableProduct(StockableProduct product);

    void updateStockableProductFromDto(UpdateStockableProductRequest request, @MappingTarget StockableProduct stockableProduct);
}
