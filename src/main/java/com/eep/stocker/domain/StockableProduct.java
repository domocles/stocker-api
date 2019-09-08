package com.eep.stocker.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class StockableProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @Column(unique = true)
    private String mpn;

    private String description;

    @NotNull
    private String category;

    @ElementCollection
    @CollectionTable(name = "material_tags", joinColumns = @JoinColumn(name = "material_id"))
    @Column(name = "tags")
    private Set<String> tags = new HashSet<>();

    private double stockPrice;

    private double inStock;

    public StockableProduct() { }

    public StockableProduct(Long id, String name, String mpn, String description, String category, Set<String> tags, double stockPrice, double inStock) {
        this.id = id;
        this.name = name;
        this.mpn = mpn;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.stockPrice = stockPrice;
        this.inStock = inStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getTags() {
        return tags;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getInStock() {
        return inStock;
    }

    public void setInStock(double inStock) {
        this.inStock = inStock;
    }

    public void addTag(String tag) {
        this.getTags().add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockableProduct)) return false;
        StockableProduct that = (StockableProduct) o;
        return Double.compare(that.getStockPrice(), getStockPrice()) == 0 &&
                Double.compare(that.getInStock(), getInStock()) == 0 &&
                getName().equals(that.getName()) &&
                getMpn().equals(that.getMpn()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                getCategory().equals(that.getCategory()) &&
                Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMpn(), getDescription(), getCategory(), getTags(), getStockPrice(), getInStock());
    }

    @Override
    public String toString() {
        return "StockableProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mpn='" + mpn + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", tags=" + tags +
                ", stockPrice=" + stockPrice +
                ", inStock=" + inStock +
                '}';
    }
}
