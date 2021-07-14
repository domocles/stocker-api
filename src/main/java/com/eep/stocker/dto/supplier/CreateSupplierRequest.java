package com.eep.stocker.dto.supplier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSupplierRequest implements SupplierDTO.SupplierName,
        SupplierDTO.DefaultCurrency,
        SupplierDTO.EmailAddress,
        SupplierDTO.TelephoneNumber {
    private String supplierName;
    private String defaultCurrency;
    private String emailAddress;
    private String telephoneNumber;
}
