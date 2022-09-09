package com.eep.stocker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity(name = "supplier_quote")
@Table(name = "supplier_quote")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierQuote extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "stockable_product_id")
    private StockableProduct stockableProduct;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private LocalDate quotationDate;
    private Double qty;
    private Double price;


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
