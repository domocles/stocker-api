package com.eep.stocker.dto.delivery;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.supplier.SupplierMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = { MapperUtils.class, SupplierMapper.class })
public interface DeliveryMapper {
    @Mapping(source = "uid", target = "id")
    @Mapping(source = "supplier.uid", target = "supplier.id")
    DeliveryDTO deliveryToDeliveryDTO(Delivery delivery);

    @Mapping(source = "uid", target = "id")
    @Mapping(source = "deliveryDate", target = "deliveryDate")
    GetDeliveryResponse deliveryToGetDeliveryResponse(Delivery delivery);

    Delivery map(CreateDeliveryRequest request);

    @Mapping(source = "supplier.uid", target = "supplierId")
    @Mapping(source = "deliveryDate", target = "deliveryDate")
    CreateDeliveryRequest map(Delivery delivery);

    @Mapping(target= "supplier", ignore = true)
    @Mapping(target= "id", ignore = true)
    @Mapping(target="uid", source="id")
    void update(UpdateDeliveryRequest request, @MappingTarget Delivery delivery);

    default GetAllDeliveryResponse getAllDeliveryReponseFromDeliveries(List<Delivery> deliveries) {
        var response = new GetAllDeliveryResponse();
        deliveries.stream().map(this::deliveryToGetDeliveryResponse).forEach(response::addDeliveryResponse);
        return response;
    }
}
