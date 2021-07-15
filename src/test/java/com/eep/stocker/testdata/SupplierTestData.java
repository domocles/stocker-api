package com.eep.stocker.testdata;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.supplier.*;

abstract public class SupplierTestData {
    public Supplier supplier;
    public Supplier shelleys;
    public Supplier ukf;
    public Supplier fji;

    public GetSupplierResponse getSupplierResponse;

    public CreateSupplierRequest createSupplierRequest;
    public CreateSupplierResponse createSupplierResponse;

    public UpdateSupplierRequest updateSupplierRequestNoId;
    public UpdateSupplierRequest updateSupplierRequest;

    public DeletedSupplierResponse deletedSupplierResponse;

    public SupplierTestData() {
        supplier = Supplier.builder()
                .id(1L)
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

        createSupplierRequest = CreateSupplierRequest.builder()
                .supplierName(supplier.getSupplierName())
                .defaultCurrency(supplier.getDefaultCurrency())
                .emailAddress(supplier.getEmailAddress())
                .telephoneNumber(supplier.getTelephoneNumber())
                .build();
        createSupplierResponse = CreateSupplierResponse.builder()
                .id(supplier.getUid().toString())
                .supplierName(supplier.getSupplierName())
                .defaultCurrency(supplier.getDefaultCurrency())
                .emailAddress(supplier.getEmailAddress())
                .telephoneNumber(supplier.getTelephoneNumber())
                .build();

        updateSupplierRequestNoId = UpdateSupplierRequest.builder()
                .supplierName(supplier.getSupplierName())
                .defaultCurrency(supplier.getDefaultCurrency())
                .emailAddress(supplier.getEmailAddress())
                .telephoneNumber(supplier.getTelephoneNumber())
                .build();

        updateSupplierRequest = UpdateSupplierRequest.builder()
                .id(supplier.getUid().toString())
                .supplierName(supplier.getSupplierName())
                .defaultCurrency(supplier.getDefaultCurrency())
                .emailAddress(supplier.getEmailAddress())
                .telephoneNumber(supplier.getTelephoneNumber())
                .build();

        deletedSupplierResponse = DeletedSupplierResponse.builder()
                .supplierName(supplier.getSupplierName())
                .defaultCurrency(supplier.getDefaultCurrency())
                .emailAddress(supplier.getEmailAddress())
                .telephoneNumber(supplier.getTelephoneNumber())
                .build();
    }
}
