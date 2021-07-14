package com.eep.stocker.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.*;

/***
 * The Stockable Product - represents any object which can sit on a shelf
 */
@Entity(name="StockableProduct")
@Table(name="stockable_product")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockableProduct {

    /***
     * Database ID of the StockableProduct
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stockable_product_id")
    private Long id;

    /***
     * The product name of the stockable product e.g. 51mm x 152mm Ilok Flex
     */
    @NotNull
    @Column(name = "stockable_product_name")
    private String name;

    /***
     * The manufacturers part number of the stockable product e.g. FX-51-152-I
     */
    @Column(unique = true, name = "stockable_product_mpn")
    private String mpn;

    /***
     * The universally unique id of the stockable product
     */
    @NaturalId
    @Column(name = "stockable_product_uid")
    @Builder.Default //lombok
    private UUID uid = UUID.randomUUID();

    /***
     * Description of the stockable products e.g. 'An ilok lined flex with a bore of 51mm and an
     * overall length of 152mm.  Stainless steel collars.'
     */
    @Column(name = "stockable_product_description")
    private String description;

    /***
     * The category of the stockable product e.g. Flex
     */
    @NotNull
    @Column(name = "stockable_product_category")
    private String category;

    /***
     * The tags of the stockable product.  A tag is a keyword that can be used to search for the
     * stockable product e.g. 'ilok, flex, fx, 51mm, 50.8mm, 2 inch, 2", 152mm, 150mm, 6 inch, 6"'
     */
    @ElementCollection
    @CollectionTable(name = "material_tags", joinColumns = @JoinColumn(name = "material_id"))
    @Column(name = "tags")
    @Singular //lombok
    private Set<String> tags = new HashSet<>();

    /***
     * The units of the stockable product e.g. 'meters' or 'flexes'
     */
    private String units = "Units";

    /***
     * The stock prices which is used to generate the stock valuation of this stockable product, the
     * stock valuation is equal to stockPrice * inStock
     */
    private double stockPrice;

    /***
     * The amount of stock we have on the shelf.  This may be deprecated when transactions come into play
     */
    private double inStock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockableProduct)) return false;
        StockableProduct that = (StockableProduct) o;
        return Double.compare(that.getStockPrice(), getStockPrice()) == 0 &&
                Double.compare(that.getInStock(), getInStock()) == 0 &&
                getName().equals(that.getName()) &&
                getMpn().equals(that.getMpn()) &&
                getUnits().equals(that.getUnits()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                getCategory().equals(that.getCategory()) &&
                Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMpn(), getDescription(), getCategory(), getTags(), getUnits(), getStockPrice(), getInStock());
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
                ", units=" + units +
                ", stockPrice=" + stockPrice +
                ", inStock=" + inStock +
                '}';
    }
}
