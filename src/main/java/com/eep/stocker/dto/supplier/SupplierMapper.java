package com.eep.stocker.dto.supplier;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface SupplierMapper {
    @Mapping(source = "uid", target = "id")
    GetSupplierResponse getSupplierResponseFromSupplier(Supplier supplier);

    @Mapping(source = "uid", target = "id")
    CreateSupplierResponse mapToCreateResponse(Supplier supplier);

    Supplier mapFromCreateRequest(CreateSupplierRequest request);

    @Mapping(source = "uid", target = "id")
    UpdateSupplierResponse mapToUpdateResponse(Supplier supplier);

    DeletedSupplierResponse mapToDeleteResponse(Supplier supplier);

    void updateFromUpdateRequest(@MappingTarget Supplier supplier, UpdateSupplierRequest request);


    default GetAllSuppliersResponse getAllSupplierResponseFromList(List<Supplier> suppliers) {
        var response = new GetAllSuppliersResponse();
        suppliers.forEach(s -> response.addGetSupplierResponse(getSupplierResponseFromSupplier(s)));
        return response;
    }
}
