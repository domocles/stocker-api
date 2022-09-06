package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.purchaseorder.PurchaseOrderMapper;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import com.eep.stocker.dto.supplier.SupplierMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MapperUtils.class, SupplierMapper.class, PurchaseOrderMapper.class, StockableProductMapper.class})
public interface PurchaseOrderLineMapper {
    @Mapping(target = "id", source = "uid")
    GetPurchaseOrderLineResponse mapToGetResponse(PurchaseOrderLine source);

    @Mapping(target = "id", source = "uid")
    UpdatePurchaseOrderLineResponse mapToUpdateResponse(PurchaseOrderLine source);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.uid")
    @Mapping(target = "stockableProductId", source = "stockableProduct.uid")
    GetPurchaseOrderLineLowDetailResponse mapToGetLowDetailResponse(PurchaseOrderLine orderLine);

    @Mapping(target = "id", ignore = true)
    PurchaseOrderLine mapCreateRequestToPurchaseOrderLine(CreatePurchaseOrderLineRequest request);

    @Mapping(target = "id", source = "uid")
    CreatePurchaseOrderLineResponse mapToCreateResponse(PurchaseOrderLine orderLine);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    void updateFromRequest(@MappingTarget PurchaseOrderLine orderLine, UpdatePurchaseOrderLineRequest request);
}
