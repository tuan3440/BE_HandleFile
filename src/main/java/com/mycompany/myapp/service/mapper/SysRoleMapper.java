package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysRole;
import com.mycompany.myapp.service.dto.SysRoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysRoleMapper extends EntityMapper<SysRoleDTO, SysRole> {}
