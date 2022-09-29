package com.eep.stocker.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/***
 * @author Sam Burns
 * @version 1.0
 * 28/09/2022
 *
 * Assembly Line domain object
 */
@Entity(name="AssemblyLine")
@Table(name="assembly_line")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLine extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "stockable_product_id")
    private StockableProduct stockableProduct;

    @ManyToOne
    @JoinColumn(name = "assembly_id")
    private Assembly assembly;

    private double qty;

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
