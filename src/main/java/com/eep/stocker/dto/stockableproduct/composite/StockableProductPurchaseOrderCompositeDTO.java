package com.eep.stocker.dto.stockableproduct.composite;

import com.eep.stocker.domain.Status;
import com.eep.stocker.dto.purchaseorder.IPurchaseOrderDTO;
import com.eep.stocker.dto.supplier.GetSupplierResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 05/10/2022
 * A response DTO for a purchase order.  Contains the purchase order lines for the order but not the full response for
 * the stockable product as this will be a composite member of a stockable product response.  It is not intended for this
 * DTO to ever be returned from an endpoint other than as a composite member of another response
 */
@Data
public class StockableProductPurchaseOrderCompositeDTO implements IPurchaseOrderDTO.Id,
        IPurchaseOrderDTO.Supplier,
        IPurchaseOrderDTO.PurchaseOrderDate,
        IPurchaseOrderDTO.PurchaseOrderReference,
        IPurchaseOrderDTO.Status,
        IStockableProductCompositeDTO.PurchaseOrderLines {
    private String id;
    private GetSupplierResponse supplier;
    private LocalDate purchaseOrderDate;
    private String purchaseOrderReference;
    private Status status;
    private List<StockableProductPurchaseOrderLineCompositeDTO> orderLines = new ArrayList<>();
}
