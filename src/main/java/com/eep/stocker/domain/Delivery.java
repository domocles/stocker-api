package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name="delivery")
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;

    private Date deliveryDate;

    private String reference;

    private String note;

    public Delivery() {}

    public Delivery(Supplier supplier, Date deliveryDate, String reference, String note) {
        this.supplier = supplier;
        this.deliveryDate = deliveryDate;
        this.reference = reference;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery)) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(getSupplier(), delivery.getSupplier()) &&
                Objects.equals(getDeliveryDate(), delivery.getDeliveryDate()) &&
                Objects.equals(getReference(), delivery.getReference()) &&
                Objects.equals(getNote(), delivery.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSupplier(), getDeliveryDate(), getReference(), getNote());
    }
}
