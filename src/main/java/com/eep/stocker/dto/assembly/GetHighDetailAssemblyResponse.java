package com.eep.stocker.dto.assembly;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 25/09/2022
 *
 * High detail response DTO for an Assembly
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class GetHighDetailAssemblyResponse implements
        AssemblyDTO.Id,
        AssemblyDTO.Category,
        AssemblyDTO.Description,
        AssemblyDTO.Mpn,
        AssemblyDTO.Name,
        AssemblyDTO.Tags,
        AssemblyDTO.SubAssemblies {
    private String id;
    private String category;
    private String description;
    private String mpn;
    private String name;

    @Singular
    private List<String> tags;

    @Singular
    private List<GetHighDetailAssemblyResponse> subAssemblies;
}
