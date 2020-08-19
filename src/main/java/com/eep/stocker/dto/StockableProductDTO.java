package com.eep.stocker.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StockableProductDTO {
    private Long id;
    private String name;
    private String mpn;
    private String description;
    private String category;
    private Set<String> tags = new HashSet<>();
    private String units = "Units";
    private double stockPrice;
    private double inStock;

    public StockableProductDTO() {

    }

    public StockableProductDTO(Long id, String name, String mpn, String description, String category, Set<String> tags, String units, double stockPrice, double inStock) {
        this.id = id;
        this.name = name;
        this.mpn = mpn;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.units = units;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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

    @Override
    public String toString() {
        return "StockableProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mpn='" + mpn + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", tags=" + tags +
                ", units='" + units + '\'' +
                ", stockPrice=" + stockPrice +
                ", inStock=" + inStock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockableProductDTO)) return false;
        StockableProductDTO that = (StockableProductDTO) o;
        return Double.compare(that.getStockPrice(), getStockPrice()) == 0 &&
                Double.compare(that.getInStock(), getInStock()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getMpn(), that.getMpn()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCategory(), that.getCategory()) &&
                Objects.equals(getTags(), that.getTags()) &&
                Objects.equals(getUnits(), that.getUnits());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMpn(), getDescription(), getCategory(), getTags(), getUnits(), getStockPrice(), getInStock());
    }
}
