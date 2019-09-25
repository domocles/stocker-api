package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity(name ="supplier")
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String supplierName;
    private String defaultCurrency = "GBP";
    private String emailAddress;
    private String telephoneNumber;

    public Supplier() {}

    public Supplier(String supplierName, String defaultCurrency, String emailAddress, String telephoneNumber) {
        this.supplierName = supplierName;
        this.defaultCurrency = defaultCurrency;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier)) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(getSupplierName(), supplier.getSupplierName()) &&
                Objects.equals(getDefaultCurrency(), supplier.getDefaultCurrency()) &&
                Objects.equals(getEmailAddress(), supplier.getEmailAddress()) &&
                Objects.equals(getTelephoneNumber(), supplier.getTelephoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSupplierName(), getDefaultCurrency(), getEmailAddress(), getTelephoneNumber());
    }
}
