package com.eep.stocker.dto.purchaseorder;

import com.eep.stocker.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePurchaseOrderRequest implements IPurchaseOrderDTO.PurchaseOrderReference,
        IPurchaseOrderDTO.SupplierId,
        IPurchaseOrderDTO.Status {
    private String purchaseOrderReference;
    private String supplierId;
    private Status status;
}
