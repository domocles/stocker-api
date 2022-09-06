package com.eep.stocker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Objects;

/***
 * The purchase order line - represents an order line e.g. what product is it for and how much will it cost
 *
 * @author Sam Burns
 * @version 1.0
 * 30/08/2022
 */
@Entity(name ="purchaseorderline")
@Table(name ="purchase_order_line")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderLine extends AbstractEntity {

    /***
     * The purchase order that this belongs to
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    /***
     * The stockable product that this order line is for
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockable_product_id")
    private StockableProduct stockableProduct;

    /***
     * Is this order line still active, should be set to closed once the final delivery has been made
     */
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    /***
     * The amount of stockable product on order
     */
    private Double qty;

    /***
     * The balance of product that is due to be delivered
     */
    private Double balance;

    /***
     * The price of the stockable product
     */
    private Double price;

    /***
     * A note for the line, any extra information that is required for the order
     */
    private String note;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseOrderLine)) return false;
        PurchaseOrderLine that = (PurchaseOrderLine) o;
        return Objects.equals(getPurchaseOrder(), that.getPurchaseOrder()) &&
                Objects.equals(getStockableProduct(), that.getStockableProduct()) &&
                Objects.equals(getQty(), that.getQty()) &&
                Objects.equals(getPrice(), that.getPrice()) &&
                Objects.equals(getNote(), that.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseOrder(), getStockableProduct(), getQty(), getPrice(), getNote());
    }
}
