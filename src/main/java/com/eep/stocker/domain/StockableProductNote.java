package com.eep.stocker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Entity(name="StockableProductNote")
@Table(name = "stockable_product_note")
public class StockableProductNote {
    @Id
    @GeneratedValue
    private Long id;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockable_product_id")
    @JsonBackReference
    private StockableProduct stockableProduct;

    public StockableProductNote() {}

    public StockableProductNote(String note, StockableProduct stockableProduct) {
        this.note = note;
        this.stockableProduct = stockableProduct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StockableProduct getStockableProduct() {
        return stockableProduct;
    }

    public void setStockableProduct(StockableProduct stockableProduct) {
        this.stockableProduct = stockableProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockableProductNote)) return false;
        StockableProductNote that = (StockableProductNote) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getNote(), that.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNote());
    }

    @Override
    public String toString() {
        return "StockableProductNote{" +
                "id=" + id +
                ", note='" + note + '\'' +
                '}';
    }
}
