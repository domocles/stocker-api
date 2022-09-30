package com.eep.stocker.dto.assembly;

import com.eep.stocker.domain.Assembly;
import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "MPN must not be blank")
    private String mpn;

    private String description;

    @NotBlank(message = "Category must not be blank")
    private String category;

    private List<String> tags = new ArrayList<>();

}
