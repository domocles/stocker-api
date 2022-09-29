package com.eep.stocker.dto.assemblyline;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 28/09/2022
 *
 * A standard response when a collection of assembly lines is required.
 */
@Data
public class GetListOfLowDetailAssemblyLineResponse {
    private List<GetLowDetailAssemblyLineResponse> assemblyLines = new ArrayList<>();

    public void addAssemblyLine(GetLowDetailAssemblyLineResponse assemblyLine) {
        assemblyLines.add(assemblyLine);
    }
}
