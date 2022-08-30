package com.eep.stocker.dto.purchaseorder;

import com.eep.stocker.dto.supplier.GetSupplierResponse;

import java.time.LocalDate;

public interface IPurchaseOrderDTO {
    interface Id{ String getId(); }
    interface PurchaseOrderReference{ String getPurchaseOrderReference(); }
    interface Supplier{ GetSupplierResponse getSupplier(); }
    interface SupplierId{ String getSupplierId(); }
    interface Status { com.eep.stocker.domain.Status getStatus(); }
    interface PurchaseOrderDate{ LocalDate getPurchaseOrderDate(); }
}
