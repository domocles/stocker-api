package com.eep.stocker.dto.delivery;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UpdateDeliveryRequest implements
        IDeliveryDTO.DeliveryDate,
        IDeliveryDTO.Reference,
        IDeliveryDTO.Note,
        IDeliveryDTO.SupplierId {
    private LocalDate deliveryDate;
    private String reference;
    private String note;
    private String supplierId;
}
