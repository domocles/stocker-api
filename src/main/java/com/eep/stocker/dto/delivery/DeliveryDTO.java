package com.eep.stocker.dto.delivery;

import com.eep.stocker.dto.supplier.SupplierDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class DeliveryDTO implements IDeliveryDTO.Id,
        IDeliveryDTO.DeliveryDate,
        IDeliveryDTO.Reference,
        IDeliveryDTO.Note,
        IDeliveryDTO.Supplier {
    private String id;
    private LocalDate deliveryDate;
    private String reference;
    private String note;
    private SupplierDTO supplier;
}
