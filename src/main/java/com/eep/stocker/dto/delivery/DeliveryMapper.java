package com.eep.stocker.dto.delivery;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.dto.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface DeliveryMapper {
    @Mapping(source = "uid", target = "id")
    @Mapping(source = "supplier.uid", target = "supplier.id")
    DeliveryDTO deliveryToDeliveryDTO(Delivery delivery);
}
