package com.eep.stocker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderTest {
    Supplier shelleys;
    Supplier ukf;
    PurchaseOrder purchaseOrder;
    Date theDate;

    @BeforeEach
    void setup() {
        shelleys = new Supplier();
        shelleys.setId(1L);
        shelleys.setTelephoneNumber("01527 578686");
        shelleys.setEmailAddress("jon.horton@shelleys.co.uk");
        shelleys.setDefaultCurrency("GBP");
        shelleys.setSupplierName("Shelley Parts Ltd");

        ukf = new Supplier();
        ukf.setId(2L);
        ukf.setTelephoneNumber("0121 288 5842");
        ukf.setEmailAddress("dipit@ukf.com");
        ukf.setDefaultCurrency("GBP");
        ukf.setSupplierName("UKF Tube Ltd");

        theDate = new Date();

        purchaseOrder = new PurchaseOrder("REF1", shelleys, theDate, Status.OPEN);
        purchaseOrder.setId(12L);
    }

    @Test
    void getId() {
        assertThat(purchaseOrder.getId()).isEqualTo(12L);
    }

    @Test
    void setId() {
        assertThat(purchaseOrder.getId()).isEqualTo(12L);
        purchaseOrder.setId(9L);
        assertThat(purchaseOrder.getId()).isEqualTo(9L);
    }

    @Test
    void getPurchaseOrderReference() {
        assertThat(purchaseOrder.getPurchaseOrderReference()).isEqualTo("REF1");
    }

    @Test
    void setPurchaseOrderReference() {
        assertThat(purchaseOrder.getPurchaseOrderReference()).isEqualTo("REF1");
        purchaseOrder.setPurchaseOrderReference("REF2");
        assertThat(purchaseOrder.getPurchaseOrderReference()).isEqualTo("REF2");
    }

    @Test
    void getSupplier() {
        assertThat(purchaseOrder.getSupplier().getSupplierName()).isEqualTo("Shelley Parts Ltd");
    }

    @Test
    void setSupplier() {
        assertThat(purchaseOrder.getSupplier().getSupplierName()).isEqualTo("Shelley Parts Ltd");
        purchaseOrder.setSupplier(ukf);
        assertThat(purchaseOrder.getSupplier().getSupplierName()).isEqualTo("UKF Tube Ltd");
    }

    @Test
    void getPurchaseOrderDate() {
        assertThat(purchaseOrder.getPurchaseOrderDate()).isEqualTo(theDate);
    }

    @Test
    void setPurchaseOrderDate() {
        Date newDate = new Date();
        assertThat(purchaseOrder.getPurchaseOrderDate()).isEqualTo(theDate);
        purchaseOrder.setPurchaseOrderDate(newDate);
        assertThat(purchaseOrder.getPurchaseOrderDate()).isEqualTo(newDate);
    }
}