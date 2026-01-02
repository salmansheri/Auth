package com.Backend.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.Backend.Auth.Models.Role;

import com.Backend.DTOs.RoleDTO;


@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class); 

    RoleDTO toDTO(Role role); 
    Role toEntity(RoleDTO roleDTO);
    
}
