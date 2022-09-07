package com.eep.stocker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/***
 * @author Sam Burns
 * @version 1.0
 * 07/09/2022
 *
 * StockTransaction domain object which represents a movement of stock
 */
@Entity(name = "StockTransaction")
@Table(name = "stock_transaction")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockTransaction extends AbstractEntity {
    /***
     *  The stockable product that this stock transaction refers to
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private StockableProduct stockableProduct;

    /***
     * The amount of the transaction
     */
    private double quantity;

    /***
     * A reference for the transaction
     */
    private String reference;

    /***
     * A note for the transaction
     */
    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockTransaction)) return false;
        StockTransaction that = (StockTransaction) o;
        return Double.compare(that.getQuantity(), getQuantity()) == 0 &&
                getStockableProduct().equals(that.getStockableProduct()) &&
                Objects.equals(getReference(), that.getReference()) &&
                Objects.equals(getNote(), that.getNote()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockableProduct(), getQuantity(), getReference(), getNote(), getCreatedAt());
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", stockableProduct=" + stockableProduct +
                ", quantity=" + quantity +
                ", reference='" + reference + '\'' +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
