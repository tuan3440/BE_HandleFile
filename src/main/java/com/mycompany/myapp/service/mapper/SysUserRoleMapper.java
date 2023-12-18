package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysAction;
import com.mycompany.myapp.domain.SysUserRole;
import com.mycompany.myapp.service.dto.SysUserRoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysUserRoleMapper extends EntityMapper<SysUserRoleDTO, SysUserRole> {}
