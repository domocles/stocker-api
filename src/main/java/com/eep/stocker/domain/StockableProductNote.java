package com.eep.stocker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import java.util.Objects;

/***
 * @author Sam Burns
 * @version 1.0
 * 15/09/2022
 * A stockable product note is a repository for notes about a stockable product
 */
@Entity(name="StockableProductNote")
@Table(name = "stockable_product_note")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockableProductNote extends AbstractEntity {
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockable_product_id")
    @JsonBackReference
    private StockableProduct stockableProduct;


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
