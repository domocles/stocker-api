package com.eep.stocker.dto.supplier;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface SupplierMapper {
    @Mapping(source = "uid", target = "id")
    GetSupplierResponse getSupplierResponseFromSupplier(Supplier supplier);

    @Mapping(source = "uid", target = "id")
    CreateSupplierResponse createSupplierResponseFromSupplier(Supplier supplier);
    Supplier createSupplierFromCreateSupplierRequest(CreateSupplierRequest request);

    @Mapping(source = "uid", target = "id")
    UpdateSupplierResponse updateSupplierResponseFromSupplier(Supplier supplier);

    CreateSupplierRequest createSupplierRequestFromUpdateSupplierRequest(UpdateSupplierRequest request);
    UpdateSupplierResponse updateSupplierResponseFromCreateSupplierResponse(CreateSupplierResponse response);

    DeletedSupplierResponse deletedSupplierResponseFromSupplier(Supplier supplier);

    @Mapping(source = "id", target = "uid")
    @Mapping(target = "id", ignore = true)
    Supplier mapFromUpdateSupplierRequest(UpdateSupplierRequest request);

    default GetAllSuppliersResponse getAllSupplierResponseFromList(List<Supplier> suppliers) {
        var response = new GetAllSuppliersResponse();
        suppliers.forEach(s -> response.addGetSupplierResponse(getSupplierResponseFromSupplier(s)));
        return response;
    }
}
