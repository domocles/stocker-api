package com.eep.stocker.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "StockTransaction")
@Table(name = "post_comment")
public class StockTransaction {
    private long id;
    private StockableProduct stockableProduct;
    private double quantity;
    private String reference;
    private String note;
    private LocalDate dateCreated;

    public StockTransaction() {
        this.stockableProduct = new StockableProduct();
        this.quantity = 0.0;
        this.reference = "";
        this.note = "";
        this.dateCreated = LocalDate.now();
    }

    public StockTransaction(StockableProduct stockableProduct, double quantity, String reference, String note, LocalDate dateCreated) {
        this.stockableProduct = stockableProduct;
        this.quantity = quantity;
        this.reference = reference;
        this.note = note;
        this.dateCreated = dateCreated;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public StockableProduct getStockableProduct() {
        return stockableProduct;
    }

    public void setStockableProduct(StockableProduct stockableProduct) {
        this.stockableProduct = stockableProduct;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockTransaction)) return false;
        StockTransaction that = (StockTransaction) o;
        return Double.compare(that.getQuantity(), getQuantity()) == 0 &&
                getStockableProduct().equals(that.getStockableProduct()) &&
                Objects.equals(getReference(), that.getReference()) &&
                Objects.equals(getNote(), that.getNote()) &&
                Objects.equals(getDateCreated(), that.getDateCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockableProduct(), getQuantity(), getReference(), getNote(), getDateCreated());
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", stockableProduct=" + stockableProduct +
                ", quantity=" + quantity +
                ", reference='" + reference + '\'' +
                ", note='" + note + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
