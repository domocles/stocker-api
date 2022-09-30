package com.eep.stocker.dto.delivery;

import com.eep.stocker.annotations.validators.ValidUUID;
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

    @ValidUUID(message = "Supplier Id must be a UUID")
    private String supplierId;
}
