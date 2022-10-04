package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.domain.Status;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateStatusRequest {
    @NotNull(message = "status cannot be null")
    private Status status;
}
