package com.eep.stocker.dto.purchaseorder;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.supplier.SupplierMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MapperUtils.class, SupplierMapper.class})
public interface PurchaseOrderMapper {
    @Mapping(source = "uid", target = "id")
    GetPurchaseOrderResponse mapGetResponse(PurchaseOrder value);

    @Mapping(source = "uid", target = "id")
    CreatePurchaseOrderResponse mapCreateResponse(PurchaseOrder value);

    @Mapping(source = "uid", target = "id")
    UpdatePurchaseOrderResponse mapToUpdateResponse(PurchaseOrder value);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target="supplier", ignore = true)
    void updateFromRequest(@MappingTarget PurchaseOrder purchaseOrder,  UpdatePurchaseOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "supplier", source = "supplier")
    void updateFromRequest(@MappingTarget PurchaseOrder purchaseOrder,  UpdatePurchaseOrderRequest request, Supplier supplier);
}
