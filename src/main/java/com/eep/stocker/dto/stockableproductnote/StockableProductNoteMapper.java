package com.eep.stocker.dto.stockableproductnote;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.StockableProductNote;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MapperUtils.class, StockableProductMapper.class})
public interface StockableProductNoteMapper {
    @Mapping(target = "id", source = "uid")
    GetStockableProductNoteResponse mapToGetResponse(StockableProductNote note);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "stockableProductId", source = "stockableProduct.uid")
    GetStockableProductNoteLowDetailResponse mapToLowDetailResponse(StockableProductNote note);

    List<GetStockableProductNoteLowDetailResponse> mapToLowDetailResponses(List<StockableProductNote> note);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "stockableProduct", source = "stockableProduct")
    StockableProductNote mapFromCreateRequest(CreateStockableProductNoteRequest request, StockableProduct stockableProduct);

    @Mapping(target = "id", source = "uid")
    CreateStockableProductNoteResponse mapToCreateResponse(StockableProductNote note);

    @Mapping(target = "id", source = "uid")
    UpdateStockableProductNoteResponse mapToUpdateResponse(StockableProductNote note);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    void updateFromUpdateRequest(@MappingTarget StockableProductNote note, UpdateStockableProductNoteRequest request);
}
