package com.eep.stocker.dto.assembly;

import com.eep.stocker.domain.Assembly;
import com.eep.stocker.dto.MapperUtils;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { MapperUtils.class })
public interface AssemblyMapper {
    @Mapping(target = "id", source = "uid")
    CreateAssemblyResponse mapTpCreateResponse(Assembly assembly);

    @Mapping(target ="id", source = "uid")
    GetAssemblyResponse mapToGetResponse(Assembly assembly);

    @Mapping(target ="id", source = "uid")
    GetAssemblyByMpnResponse mapToGetByMpnResponse(Assembly assembly);

    @Mapping(target = "id", source = "uid")
    @Named("mapToHighDetailResponse")
    GetHighDetailAssemblyResponse mapToHighDetailResponse(Assembly assembly);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "subAssemblyIds", source = "subAssemblies")
    GetLowDetailAssemblyResponse mapToGetLowDetailResponse(Assembly assembly);

    @IterableMapping(qualifiedByName = "mapToHighDetailResponse")
    List<GetHighDetailAssemblyResponse> mapToList(List<Assembly> assemblies);

    @Mapping(target = "id", source = "uid")
    UpdateAssemblyResponse mapToUpdateResponse(Assembly assembly);

    Assembly updateFromUpdateRequest(@MappingTarget Assembly assembly, UpdateAssemblyRequest request);

    Assembly mapFromCreateRequest(CreateAssemblyRequest request);

    default String mapSubAssemblyToString(Assembly assembly) {
        return assembly.getUid().toString();
    }
}
