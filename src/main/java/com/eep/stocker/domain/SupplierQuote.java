package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "supplier_quote")
@Table(name = "supplier_quote")
public class SupplierQuote {
    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "stockable_product_id")
    private StockableProduct stockableProduct;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private Date quotationDate;
    private Double qty;
    private Double price;

    public SupplierQuote() {}

    public SupplierQuote(StockableProduct stockableProduct, Supplier supplier, Date quotationDate, Double qty, Double price) {
        this.stockableProduct = stockableProduct;
        this.supplier = supplier;
        this.quotationDate = quotationDate;
        this.qty = qty;
        this.price = price;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public StockableProduct getStockableProduct() {
        return stockableProduct;
    }

    public void setStockableProduct(StockableProduct stockableProduct) {
        this.stockableProduct = stockableProduct;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierQuote)) return false;
        SupplierQuote that = (SupplierQuote) o;
        return Objects.equals(getStockableProduct(), that.getStockableProduct()) &&
                Objects.equals(getSupplier(), that.getSupplier()) &&
                Objects.equals(getQuotationDate(), that.getQuotationDate()) &&
                Objects.equals(getQty(), that.getQty()) &&
                Objects.equals(getPrice(), that.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockableProduct(), getSupplier(), getQuotationDate(), getQty(), getPrice());
    }

    @Override
    public String toString() {
        return "SupplierQuote{" +
                "stockableProduct=" + stockableProduct +
                ", supplier=" + supplier +
                ", quotationDate=" + quotationDate +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
