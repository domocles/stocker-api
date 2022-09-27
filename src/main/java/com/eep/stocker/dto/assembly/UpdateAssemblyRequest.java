package com.eep.stocker.dto.assembly;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * Request DTO for the update assembly endpoint
 */
@Data
public class UpdateAssemblyRequest implements
        AssemblyDTO.Name,
        AssemblyDTO.Mpn,
        AssemblyDTO.Description,
        AssemblyDTO.Category,
        AssemblyDTO.Tags {
    private String name;
    private String mpn;
    private String description;
    private String category;
    private List<String> tags = new ArrayList<>();
}
