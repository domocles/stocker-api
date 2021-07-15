package com.eep.stocker.dto.supplier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletedSupplierResponse implements ISupplierDTO.SupplierName,
        ISupplierDTO.DefaultCurrency,
        ISupplierDTO.EmailAddress,
        ISupplierDTO.TelephoneNumber {
    private String supplierName;
    private String defaultCurrency;
    private String emailAddress;
    private String telephoneNumber;
}
