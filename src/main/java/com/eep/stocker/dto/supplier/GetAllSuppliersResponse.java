package com.eep.stocker.dto.supplier;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllSuppliersResponse {
    private List<GetSupplierResponse> allSuppliers = new ArrayList<>();

    public void addGetSupplierResponse(GetSupplierResponse supplierResponse) {
        this.allSuppliers.add(supplierResponse);
    }
}
