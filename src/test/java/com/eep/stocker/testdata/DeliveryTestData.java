package com.eep.stocker.testdata;

import com.eep.stocker.domain.Delivery;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.dto.delivery.DeliveryDTO;
import com.eep.stocker.dto.delivery.GetDeliveryResponse;
import com.eep.stocker.dto.supplier.SupplierDTO;

import java.time.LocalDate;


public class DeliveryTestData {
    public static Supplier shelleys = Supplier.builder()
            //.id(1L)
            .supplierName("Shelley Parts Ltd")
            .defaultCurrency("GBP")
            .emailAddress("jon.horton@shelleyparts.co.uk")
            .telephoneNumber("01527 584285")
            .build();
    public static Supplier ukf = Supplier.builder()
            //.id(2L)
            .supplierName("UKF Ltd")
            .defaultCurrency("GBP")
            .emailAddress("sales@ukf-group.com")
            .telephoneNumber("01527 578686")
            .build();
    public static Delivery delivery1 = Delivery.builder()
            //.id(1L)
            .deliveryDate(LocalDate.of(2021, 07, 15))
            .reference("testdelivery")
            .note("A test delivery")
            .supplier(shelleys)
            .build();

    public static Delivery delivery2 = Delivery.builder()
            //.id(2L)
            .deliveryDate(LocalDate.of(2021, 05, 15))
            .reference("testdelivery2")
            .note("A second test delivery")
            .supplier(ukf)
            .build();
    public static Delivery delivery3 = Delivery.builder()
            //.id(3L)
            .deliveryDate(LocalDate.of(2021, 8, 15))
            .reference("testdelivery3")
            .note("A third test delivery")
            .supplier(ukf)
            .build();
    public static SupplierDTO supplierDTO = SupplierDTO.builder()
            .id(shelleys.getUid().toString())
            .supplierName("Shelley Parts Ltd")
            .defaultCurrency("GBP")
            .emailAddress("jon.horton@shelleyparts.co.uk")
            .telephoneNumber("01527 584285")
            .build();
    public static GetDeliveryResponse getDeliveryResponse = GetDeliveryResponse.builder()
            .id(delivery1.getUid().toString())
            .deliveryDate(LocalDate.of(2021, 07, 15))
            .reference("testdelivery")
            .note("A test delivery")
            .supplier(supplierDTO)
            .build();
    public static DeliveryDTO deliveryDTO = DeliveryDTO.builder()
            .id(delivery1.getUid().toString())
            .deliveryDate(LocalDate.of(2021, 07, 15))
            .reference("testdelivery")
            .note("A test delivery")
            .supplier(supplierDTO)
            .build();
}
