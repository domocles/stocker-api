package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "deliveryline")
@Table(name = "delivery_line")
public class DeliveryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_line_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_line_id")
    private PurchaseOrderLine purchaseOrderLine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private Double quantityDelivered;

    private String note;

    public DeliveryLine() {}

    public DeliveryLine(PurchaseOrderLine purchaseOrderLine, Delivery delivery, Double quantityDelivered, String note) {
        this.purchaseOrderLine = purchaseOrderLine;
        this.delivery = delivery;
        this.quantityDelivered = quantityDelivered;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrderLine getPurchaseOrderLine() {
        return purchaseOrderLine;
    }

    public void setPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        this.purchaseOrderLine = purchaseOrderLine;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Double getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDelivered(Double quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryLine)) return false;
        DeliveryLine that = (DeliveryLine) o;
        return Objects.equals(getPurchaseOrderLine(), that.getPurchaseOrderLine()) &&
                Objects.equals(getDelivery(), that.getDelivery()) &&
                Objects.equals(getQuantityDelivered(), that.getQuantityDelivered()) &&
                Objects.equals(getNote(), that.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseOrderLine(), getDelivery(), getQuantityDelivered(), getNote());
    }

    @Override
    public String toString() {
        return "DeliveryLine{" +
                "id=" + id +
                ", purchaseOrderLine=" + purchaseOrderLine +
                ", delivery=" + delivery +
                ", quantityDelivered=" + quantityDelivered +
                ", note='" + note + '\'' +
                '}';
    }
}
