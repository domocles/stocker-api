package com.eep.stocker.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSupplierResponse implements ISupplierDTO.Id,
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
