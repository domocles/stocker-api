package com.eep.stocker.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {
    /***
     * Unique ID for the address
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, unique = true)
    protected Long id;

    /**
     * Unique global id of the address
     */
    @Column(unique = true, nullable = false, length = 16)
    @Builder.Default
    @Type(type = "org.hibernate.type.UUIDBinaryType")
    protected UUID uid = UUID.randomUUID();

    /***
     * Optimistic locking control
     */
    @Version
    protected int version;

    @NotNull
    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(name = "modified_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDateTime modifiedAt;

    /***
     * sets the createdAt time.  We don't have access to the Spring @CreatedAt so using Hibernate
     */
    @PrePersist
    protected void prePersist() { modifiedAt = LocalDateTime.now(); }

    /***
     * updates the updatedAt time when an update is performed
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof AbstractEntity)) return false;
        AbstractEntity that = (AbstractEntity) o;
        return uid.equals(that.uid);
    }

    @Override
    public int hashCode() { return Objects.hash(uid); }
}
