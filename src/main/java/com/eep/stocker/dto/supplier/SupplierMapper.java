package com.eep.stocker.dto.supplier;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface SupplierMapper {
    @Mapping(source = "uid", target = "id")
    GetSupplierResponse getSupplierResponseFromSupplier(Supplier supplier);
}
