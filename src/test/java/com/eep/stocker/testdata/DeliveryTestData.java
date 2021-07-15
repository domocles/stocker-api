package com.eep.stocker.testdata;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;

import java.time.LocalDate;


public class DeliveryTestData {
    public static Supplier shelleys = Supplier.builder()
            .id(1L)
            .supplierName("Shelley Parts Ltd")
            .defaultCurrency("GBP")
            .emailAddress("jon.horton@shelleyparts.co.uk")
            .telephoneNumber("01527 584285")
            .build();;
    public static Delivery delivery1 = Delivery.builder()
            .id(1L)
            .deliveryDate(LocalDate.of(2021, 07, 15))
            .reference("testdelivery")
            .note("A test delivery")
            .supplier(shelleys)
            .build();

}
