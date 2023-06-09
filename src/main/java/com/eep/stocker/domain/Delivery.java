package com.eep.stocker.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity(name="delivery")
@Table(name = "delivery")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends AbstractEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier supplier;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "delivery_reference")
    private String reference;

    @Column(name = "delivery_note")
    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery)) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(getUid(), delivery.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }
}
