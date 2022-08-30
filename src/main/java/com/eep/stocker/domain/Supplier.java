package com.eep.stocker.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/***
 * A supplier is a supplier of stockable products.
 */
@Entity(name ="supplier")
@Table(name = "supplier")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier implements Serializable {
    /***
     * Database ID of the supplier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "supplier_id")
    private Long id;

    /***
     * Universally unique id of the supplier
     */
    @NaturalId
    @Builder.Default
    @Column(name = "supplier_uid")
    private UUID uid = UUID.randomUUID();

    /***
     * The name of the supplier e.g. Shelleys
     */
    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    /***
     * Currency which the supplier uses, e.g. GBP
     */
    @Column(name = "supplier_currency")
    private String defaultCurrency = "GBP";

    /***
     * The email address used to contact the supplier
     */
    @Column(name = "supplier_email")
    @Email
    private String emailAddress;

    /***
     * The telephone number used to contact the supplier
     */
    @Column(name = "supplier_telephone")
    private String telephoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier)) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(getUid(), supplier.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }
}
