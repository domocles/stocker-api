package com.eep.stocker.dto;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MapperUtils {
    default String uuidToString(UUID uuid) { return uuid.toString(); }

    default UUID stringToUUID(String uuid) { return UUID.fromString(uuid); }
}
