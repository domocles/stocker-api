package com.eep.stocker.dto.assemblyline;

import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.assembly.AssemblyMapper;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { MapperUtils.class, StockableProductMapper.class, AssemblyMapper.class })
public interface AssemblyLineMapper {

    @Mapping(target = "id", source = "uid")
    DeleteAssemblyLineResponse mapToDeleteResponse(AssemblyLine assemblyLine);

    @Mapping(target = "id", source = "uid")
    CreateAssemblyLineResponse mapToCreateResponse(AssemblyLine assemblyLine);

    @Mapping(target = "id", source = "uid")
    GetAssemblyLineResponse mapToGetResponse(AssemblyLine assemblyLine);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "assemblyId", source = "assembly.uid")
    GetLowDetailAssemblyLineResponse mapToLowDetailResponse(AssemblyLine assemblyLine);

    @Mapping(target = "id", source = "uid")
    UpdateAssemblyLineResponse mapToUpdateResponse(AssemblyLine assemblyLine);

    @Mapping(target = "id", ignore = true)
    AssemblyLine mapFromCreateRequest(CreateAssemblyLineRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target="stockableProduct", ignore = true)
    @Mapping(target="assembly", ignore = true)
    AssemblyLine updateFromUpdateRequest(@MappingTarget AssemblyLine assemblyLine, UpdateAssemblyLineRequest request);



    default String mapToId(StockableProduct stockableProduct) {
        return stockableProduct.getUid().toString();
    }
}
