package com.eep.stocker.dto.supplier;

public interface ISupplierDTO {
    interface Id { String getId(); }
    interface SupplierName { String getSupplierName(); }
    interface DefaultCurrency { String getDefaultCurrency(); }
    interface EmailAddress { String getEmailAddress(); }
    interface TelephoneNumber { String getTelephoneNumber(); }
}
