package com.eep.stocker.dto.assembly;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 26/09/2022
 * A standard response when a collection of assemblies is required.
 */
@Data
public class GetListOfLowDetailAssemblyResponse {
    private final List<GetLowDetailAssemblyResponse> assemblies = new ArrayList<>();

    public void addAssembly(GetLowDetailAssemblyResponse response) {
        this.assemblies.add(response);
    }
}
