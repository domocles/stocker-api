package com.eep.stocker.dto.delivery;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.dto.supplier.SupplierDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateDeliveryRequest implements IDeliveryDTO.SupplierId,
        IDeliveryDTO.DeliveryDate,
        IDeliveryDTO.Note,
        IDeliveryDTO.Reference {
    @ValidUUID(message = "Supplier Id must be a UUID")
    private String supplierId;

    private LocalDate deliveryDate;

    private String note;

    private String reference;
}
