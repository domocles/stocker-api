package com.eep.stocker.dto.stocktransaction;

import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { MapperUtils.class, StockableProductMapper.class })
public interface StockTransactionMapper {
    @Mapping(target = "id", source = "uid")
    GetStockTransactionResponse mapToGetResponse(StockTransaction transaction);

    @Mapping(target = "id", source = "uid")
    CreateStockTransactionResponse mapToCreateResponse(StockTransaction transaction);

    @Mapping(target = "id", source = "uid")
    UpdateStockTransactionResponse mapToUpdateResponse(StockTransaction transaction);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "stockableProductId", source = "stockableProduct.uid")
    GetStockTransactionLowDetailResponse mapToLowDetailResponse(StockTransaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stockableProduct", source = "stockableProduct")
    StockTransaction mapCreateToStockTransaction(CreateStockTransactionRequest request, StockableProduct stockableProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    void updateFromRequest(@MappingTarget StockTransaction stockTransaction, UpdateStockTransactionRequest request);
}
