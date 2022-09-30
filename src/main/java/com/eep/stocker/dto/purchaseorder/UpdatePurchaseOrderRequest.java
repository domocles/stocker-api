package com.eep.stocker.dto.purchaseorder;

import com.eep.stocker.annotations.validators.ValidUUID;
import com.eep.stocker.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePurchaseOrderRequest implements IPurchaseOrderDTO.PurchaseOrderReference,
        IPurchaseOrderDTO.SupplierId,
        IPurchaseOrderDTO.Status {
    private String purchaseOrderReference;

    @ValidUUID(message = "Supplier Id must be a UUID")
    private String supplierId;

    private Status status;
}
