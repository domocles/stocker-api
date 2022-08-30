package com.eep.stocker.dto.purchaseorder;

import com.eep.stocker.domain.Status;
import com.eep.stocker.dto.supplier.GetSupplierResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/***
 * @author Sam Burns
 * @version 1.0
 * 24/08/2022
 *
 * DTO for a purchase order
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPurchaseOrderResponse implements
        IPurchaseOrderDTO.Id,
        IPurchaseOrderDTO.PurchaseOrderReference,
        IPurchaseOrderDTO.Supplier,
        IPurchaseOrderDTO.Status,
        IPurchaseOrderDTO.PurchaseOrderDate {
    private String id;
    private String purchaseOrderReference;
    private GetSupplierResponse supplier;
    private Status status;
    private LocalDate purchaseOrderDate;
}
