package com.eep.stocker.dto.assembly;

import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 24/09/2022
 *
 * DTO for the Assembly
 */
public interface AssemblyDTO {
    interface Id { String getId(); }
    interface Name { String getName(); }
    interface Mpn { String getMpn(); }
    interface Description { String getDescription(); }
    interface Category { String getCategory(); }
    interface Tags { List<String> getTags(); }
    interface SubAssemblies { List<GetHighDetailAssemblyResponse> getSubAssemblies(); }
    interface SubAssemblyIds { List<String> getSubAssemblyIds(); }
}
