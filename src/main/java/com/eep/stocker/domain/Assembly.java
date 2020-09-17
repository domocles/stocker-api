package com.eep.stocker.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity(name="assembly")
@Table(name="assembly")
public class Assembly {

    private Long id;
    private String name;
    private String description;

    public Assembly() {}

    public Assembly(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Assembly(Long id, String name) {
        this(id, name, "");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assembly_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assembly)) return false;
        Assembly assembly = (Assembly) o;
        return Objects.equals(getId(), assembly.getId()) &&
                Objects.equals(getName(), assembly.getName()) &&
                Objects.equals(getDescription(), assembly.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "Assembly{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
