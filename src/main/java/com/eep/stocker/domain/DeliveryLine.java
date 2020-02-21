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

    @OneToOne
    @JoinColumn(name = "stock_transaction_id")
    private StockTransaction stockTransaction;

    private Double quantityDelivered;

    private String note;

    public DeliveryLine() {
        this.stockTransaction = new StockTransaction();
    }

    public DeliveryLine(PurchaseOrderLine purchaseOrderLine,
                        Delivery delivery,
                        Double quantityDelivered,
                        String note,
                        StockTransaction stockTransaction) {
        this.purchaseOrderLine = purchaseOrderLine;
        this.delivery = delivery;
        this.quantityDelivered = quantityDelivered;
        this.note = note;
        this.stockTransaction = stockTransaction;
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

    public StockTransaction getStockTransaction() {
        return stockTransaction;
    }

    public void setStockTransaction(StockTransaction stockTransaction) {
        this.stockTransaction = stockTransaction;
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
