package com.eep.stocker.dto.supplier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSupplierRequest implements ISupplierDTO.Id,
        ISupplierDTO.SupplierName,
        ISupplierDTO.DefaultCurrency,
        ISupplierDTO.EmailAddress,
        ISupplierDTO.TelephoneNumber {
    private String id;
    private String supplierName;
    private String defaultCurrency;
    private String emailAddress;
    private String telephoneNumber;
}
