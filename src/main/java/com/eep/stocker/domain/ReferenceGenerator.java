package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "ReferenceGenerator")
public class ReferenceGenerator {
    private Long id;
    private String referenceName;
    private String prefix;
    private Long number;

    public ReferenceGenerator() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique = true)
    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceGenerator)) return false;
        ReferenceGenerator that = (ReferenceGenerator) o;
        return Objects.equals(getReferenceName(), that.getReferenceName()) &&
                Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReferenceName(), getNumber());
    }

    public String generate() {
        return String.format("%s%07d", prefix, number++);
    }
}
