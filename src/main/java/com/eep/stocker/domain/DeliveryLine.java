package com.eep.stocker.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "deliveryline")
@Table(name = "delivery_line")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryLine extends AbstractEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_line_id")
    private PurchaseOrderLine purchaseOrderLine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "stock_transaction_id")
    private StockTransaction stockTransaction;

    private Double quantityDelivered;

    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryLine)) return false;
        DeliveryLine that = (DeliveryLine) o;
        return Objects.equals(getPurchaseOrderLine(), that.getPurchaseOrderLine()) &&
                Objects.equals(getDelivery(), that.getDelivery()) &&
                Objects.equals(getStockTransaction(), that.getStockTransaction()) &&
                Objects.equals(getQuantityDelivered(), that.getQuantityDelivered()) &&
                Objects.equals(getNote(), that.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseOrderLine(), getDelivery(), getStockTransaction(), getQuantityDelivered(), getNote());
    }

    @Override
    public String toString() {
        return "DeliveryLine{" +
                "id=" + id +
                ", purchaseOrderLine=" + purchaseOrderLine +
                ", delivery=" + delivery +
                ", stockTransaction=" + stockTransaction +
                ", quantityDelivered=" + quantityDelivered +
                ", note='" + note + '\'' +
                '}';
    }
}
