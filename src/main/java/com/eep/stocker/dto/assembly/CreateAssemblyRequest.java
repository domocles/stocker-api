package com.eep.stocker.dto.assembly;

import com.eep.stocker.domain.Assembly;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 27/09/2022
 *
 * Request DTO for the Create Assembly end point
 */
@Data
public class CreateAssemblyRequest implements
        AssemblyDTO.Name,
        AssemblyDTO.Mpn,
        AssemblyDTO.Description,
        AssemblyDTO.Category {
    private String name;
    private String mpn;
    private String description;
    private String category;
    private List<String> tags = new ArrayList<>();

}
