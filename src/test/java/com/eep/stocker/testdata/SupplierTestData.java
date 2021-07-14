package com.eep.stocker.testdata;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.supplier.GetSupplierResponse;

abstract public class SupplierTestData {
    public Supplier supplier;
    public Supplier shelleys;
    public Supplier ukf;
    public Supplier fji;

    public GetSupplierResponse getSupplierResponse;

    public SupplierTestData() {
        supplier = Supplier.builder()
                .supplierName("Shelley Parts Ltd")
                .defaultCurrency("GBP")
                .emailAddress("jon.horton@shelleyparts.co.uk")
                .telephoneNumber("01527 584285")
                .build();

        shelleys = Supplier.builder()
                .supplierName("Shelley Parts Ltd")
                .defaultCurrency("GBP")
                .emailAddress("jon.horton@shelleyparts.co.uk")
                .telephoneNumber("01527 584285")
                .build();

        ukf = Supplier.builder()
            .supplierName("UKF Ltd")
            .defaultCurrency("GBP")
            .emailAddress("sales@ukf-group.com")
            .telephoneNumber("01527 578686")
            .build();

        getSupplierResponse = GetSupplierResponse.builder()
                .id(supplier.getUid().toString())
                .supplierName(supplier.getSupplierName())
                .defaultCurrency(supplier.getDefaultCurrency())
                .emailAddress(supplier.getEmailAddress())
                .telephoneNumber(supplier.getTelephoneNumber())
                .build();

    }
}
