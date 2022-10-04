package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.services.StockableProductService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = { MapperUtils.class })
public interface StockableProductMapper {
    CreateStockableProductRequest mapToCreateRequest(StockableProduct stockableProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    StockableProduct mapFromCreateRequest(CreateStockableProductRequest request);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", constant = "0.0")
    CreateStockableProductResponse mapToCreateResponse(StockableProduct product);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", source = "onOrder", defaultValue = "0.0")
    GetStockableProductResponse mapToGetResponse(StockableProduct product, Double onOrder);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", source = "onOrder", defaultValue = "0.0")
    UpdateStockableProductResponse mapToUpdateResponse(StockableProduct product, Double onOrder);

    void updateFromUpdateRequest(@MappingTarget StockableProduct stockableProduct,
                                 UpdateStockableProductRequest request);
}
