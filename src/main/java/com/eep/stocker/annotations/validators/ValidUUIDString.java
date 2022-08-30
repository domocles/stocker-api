package com.eep.stocker.annotations.validators;

import lombok.Data;

@Data
public class ValidUUIDString {
    @ValidUUID
    private String uuid;
}
