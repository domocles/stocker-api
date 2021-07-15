package com.eep.stocker.dto.delivery;

import com.eep.stocker.dto.supplier.SupplierDTO;

import java.time.LocalDate;

public interface IDeliveryDTO {
    interface Id{ String getId(); }
    interface DeliveryDate { LocalDate getDeliveryDate(); }
    interface Reference { String getReference(); }
    interface Note { String getNote(); }
    interface SupplierId { String getSupplierId(); }
    interface Supplier { SupplierDTO getSupplier(); }
}
