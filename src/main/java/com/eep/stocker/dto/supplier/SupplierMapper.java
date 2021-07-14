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


    default GetAllSuppliersResponse getAllSupplierResponseFromList(List<Supplier> suppliers) {
        var response = new GetAllSuppliersResponse();
        suppliers.forEach(s -> response.addGetSupplierResponse(getSupplierResponseFromSupplier(s)));
        return response;
    }
}
