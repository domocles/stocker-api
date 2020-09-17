package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity(name="AssemblyLine")
@Table(name="assembly_line")
public class AssemblyLine {
    private Long id;
    private StockableProduct stockableProduct;
    private Assembly assembly;
    private double qty;

    public AssemblyLine() {}

    public AssemblyLine(Long id, StockableProduct stockableProduct, Assembly assembly, double qty) {
        this.id = id;
        this.stockableProduct = stockableProduct;
        this.assembly = assembly;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assembly_line_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "stockable_product_id")
    public StockableProduct getStockableProduct() {
        return stockableProduct;
    }

    public void setStockableProduct(StockableProduct stockableProduct) {
        this.stockableProduct = stockableProduct;
    }

    @ManyToOne
    @JoinColumn(name = "assembly_id")
    public Assembly getAssembly() { return assembly; }

    public void setAssembly(Assembly assembly) { this.assembly = assembly; }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssemblyLine)) return false;
        AssemblyLine that = (AssemblyLine) o;
        return Double.compare(that.getQty(), getQty()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getStockableProduct(), that.getStockableProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStockableProduct(), getQty());
    }

    @Override
    public String toString() {
        return "AssemblyLine{" +
                "id=" + id +
                ", stockableProduct=" + stockableProduct +
                ", qty=" + qty +
                '}';
    }
}
