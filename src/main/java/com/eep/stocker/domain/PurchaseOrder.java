package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "purchaseorder")
@Table(name = "purchaseorder")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String purchaseOrderReference;

    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;

    private Date purchaseOrderDate;

    public PurchaseOrder() {}

    public PurchaseOrder(String purchaseOrderReference, Supplier supplier, Date purchaseOrderDate) {
        this.purchaseOrderReference = purchaseOrderReference;
        this.supplier = supplier;
        this.purchaseOrderDate = purchaseOrderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchaseOrderReference() {
        return purchaseOrderReference;
    }

    public void setPurchaseOrderReference(String purchaseOrderReference) {
        this.purchaseOrderReference = purchaseOrderReference;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(Date purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseOrder)) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return Objects.equals(getPurchaseOrderReference(), that.getPurchaseOrderReference()) &&
                Objects.equals(getSupplier(), that.getSupplier()) &&
                Objects.equals(getPurchaseOrderDate(), that.getPurchaseOrderDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseOrderReference(), getSupplier(), getPurchaseOrderDate());
    }
}
