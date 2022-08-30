package com.eep.stocker.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/***
 * The Purchase Order - represents an order for several products which can be purchased
 */
@Entity(name = "purchaseorder")
@Table(name = "purchase_order")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder extends AbstractEntity {

    /***
     * The purchase order reference of the purchase order e.g. PO00001
     */
    @NaturalId
    private String purchaseOrderReference;

    /***
     * The supplier order reference for the purchase order which will be different to our purchase order reference
     */
    private String supplierOrderReference;

    /***
     * Supplier of the purchase order e.g. UKF
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;

    /***
     * The date the purchase order was made
     */
    private LocalDate purchaseOrderDate;

    /***
     * The status of the purchase order
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.OPEN;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseOrder)) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return Objects.equals(getPurchaseOrderReference(), that.getPurchaseOrderReference()) &&
                Objects.equals(getSupplier(), that.getSupplier()) &&
                Objects.equals(getPurchaseOrderDate(), that.getPurchaseOrderDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseOrderReference(), getSupplier(), getPurchaseOrderDate());
    }
}
