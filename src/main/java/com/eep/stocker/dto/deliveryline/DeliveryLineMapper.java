package com.eep.stocker.dto.deliveryline;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.DeliveryLine;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockTransaction;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.delivery.DeliveryMapper;
import com.eep.stocker.dto.purchaseorderline.PurchaseOrderLineMapper;
import com.eep.stocker.dto.stocktransaction.StockTransactionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/***
 * @author Sam Burns
 * @version 1.0
 * 15/09/2022
 * Mapper for converting between requests and domain objects for a Delivery Line
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class, PurchaseOrderLineMapper.class, DeliveryMapper.class, StockTransactionMapper.class})
public interface DeliveryLineMapper {
    @Mapping(target = "id", source = "uid")
    GetDeliveryLineResponse mapToGetResponse(DeliveryLine line);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "purchaseOrderLineId", source = "purchaseOrderLine.uid")
    @Mapping(target = "deliveryId", source = "delivery.uid")
    @Mapping(target = "stockTransactionId", source = "stockTransaction.uid")
    GetLowDetailDeliveryLineResponse mapToLowDetailResponse(DeliveryLine line);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "note", source = "request.note")
    DeliveryLine mapFromCreateRequest(CreateDeliveryLineRequest request,
                                      PurchaseOrderLine purchaseOrderLine,
                                      Delivery delivery,
                                      StockTransaction stockTransaction);

    @Mapping(target = "id", source = "uid")
    CreateDeliveryLineResponse mapToCreateResponse(DeliveryLine line);

    @Mapping(target = "id", source = "uid")
    UpdateDeliveryLineResponse mapToUpdateResponse(DeliveryLine line);

    void updateFromUpdateRequest(@MappingTarget DeliveryLine deliveryLine, UpdateDeliveryLineRequest request);
}
