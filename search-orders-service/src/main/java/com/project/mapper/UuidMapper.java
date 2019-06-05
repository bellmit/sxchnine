package com.project.mapper;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public class UuidMapper {

    String asString(UUID uuid){
        return uuid.toString();
    }
}
