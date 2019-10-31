package com.eep.stocker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity(name ="purchaseorderline")
@Table(name ="purchase_order_line")
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockable_product_id")
    private StockableProduct stockableProduct;

    private Double qty;
    private Double price;
    private String note;

    public PurchaseOrderLine() {}

    public PurchaseOrderLine(Long id, PurchaseOrder purchaseOrder, StockableProduct stockableProduct, Double qty, Double price, String note) {
        this.id = id;
        this.purchaseOrder = purchaseOrder;
        this.stockableProduct = stockableProduct;
        this.qty = qty;
        this.price = price;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public StockableProduct getStockableProduct() {
        return stockableProduct;
    }

    public void setStockableProduct(StockableProduct stockableProduct) {
        this.stockableProduct = stockableProduct;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        if (!(o instanceof PurchaseOrderLine)) return false;
        PurchaseOrderLine that = (PurchaseOrderLine) o;
        return Objects.equals(getPurchaseOrder(), that.getPurchaseOrder()) &&
                Objects.equals(getStockableProduct(), that.getStockableProduct()) &&
                Objects.equals(getQty(), that.getQty()) &&
                Objects.equals(getPrice(), that.getPrice()) &&
                Objects.equals(getNote(), that.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseOrder(), getStockableProduct(), getQty(), getPrice(), getNote());
    }
}
