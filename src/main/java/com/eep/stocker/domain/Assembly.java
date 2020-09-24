package com.eep.stocker.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name="assembly")
@Table(name="assembly")
public class Assembly {

    private Long id;
    private String name;
    private String mpn;
    private String description;
    private String category;
    private Set<String> tags = new HashSet<>();


    public Assembly() {}

    public Assembly(Long id, String name, String description, String mpn, String category, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mpn = mpn;
        this.category = category;
        this.tags = tags;
    }

    public Assembly(Long id, String name, String mpn, String category, Set<String> tags) {
        this(id, name, "", mpn, category, tags);
    }

    public Assembly(Long id, String name, String mpn, String category) {
        this(id, name, "", mpn, category, Collections.EMPTY_SET);
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
        this.category = category;
        this.tags = tags;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    @NotNull
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @ElementCollection
    @CollectionTable(name = "assembly_tags", joinColumns = @JoinColumn(name = "assembly_id"))
    @Column(name = "tags")
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assembly)) return false;
        Assembly assembly = (Assembly) o;
        return Objects.equals(getId(), assembly.getId()) &&
                Objects.equals(getName(), assembly.getName()) &&
                Objects.equals(getMpn(), assembly.getMpn()) &&
                Objects.equals(getDescription(), assembly.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMpn(), getDescription());
    }

    @Override
    public String toString() {
        return "Assembly{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mpn='" + mpn + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
