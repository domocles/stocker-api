package com.eep.stocker.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/***
 * @author Sam Burns
 * @version 1.0
 * 24/09/2022
 *
 * The assembly domain object.  An assembly is a collection of assemblies, referred to as subassemblies.
 */
@Entity(name="assembly")
@Table(name="assembly")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assembly extends AbstractEntity {
    /***
     * The name of the assembly e.g. HA236
     */
    private String name;

    /***
     * The manufacturer part number of th assembly e.g. EEP236
     */
    @NaturalId
    private String mpn;

    /***
     * A description of the assembly e.g. Honda Accord back box
     */
    private String description;

    /***
     * Category for the assembly, e.g. Exhaust Parts
     */
    @NotNull
    private String category;

    /***
     * A collection of tags that help identify the part
     */
    @ElementCollection
    @CollectionTable(name = "assembly_tags", joinColumns = @JoinColumn(name = "assembly_id"))
    @Column(name = "tags")
    @Singular
    private Set<String> tags = new HashSet<>();

    /***
     * A collection of subassemblies
     */
    @ManyToMany
    @JoinTable(name = "Assembly_Subassembly",
            joinColumns = {@JoinColumn(name = "fk_assembly")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subassembly")})
    @Singular
    private List<Assembly> subAssemblies = new ArrayList<>();


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
