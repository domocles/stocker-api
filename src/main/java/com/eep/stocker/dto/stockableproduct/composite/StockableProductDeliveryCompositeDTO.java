package com.eep.stocker.dto.stockableproduct.composite;

import com.eep.stocker.dto.delivery.IDeliveryDTO;
import com.eep.stocker.dto.supplier.GetSupplierResponse;
import com.eep.stocker.dto.supplier.SupplierDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/10/2022
 * A response DTO for a deliveryr.  Contains the delivery lines for the delivery but not the full response for
 * the stockable product as this will be a composite member of a stockable product response.  It is not intended for this
 * DTO to ever be returned from an endpoint other than as a composite member of another response
 */
@Data
public class StockableProductDeliveryCompositeDTO implements
        IDeliveryDTO.Id,
        IDeliveryDTO.DeliveryDate,
        IDeliveryDTO.Supplier,
        IDeliveryDTO.Reference,
        IDeliveryDTO.Note,
        IStockableProductCompositeDTO.DeliveryLines {
    private String id;
    private LocalDate deliveryDate;
    private SupplierDTO supplier;
    private String reference;
    private String note;
    private List<StockableProductDeliveryLineCompositeDTO> deliveryLines = new ArrayList<>();
}
