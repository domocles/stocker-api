package com.eep.stocker.dto.purchaseorder;

import com.eep.stocker.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * @author Sam Burns
 * @version 1.0
 * 04/10/2022
 * Request DTO for the update purchase order status end point
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePurchaseOrderStatusRequest implements IPurchaseOrderDTO.Status {
    private Status status;
}
