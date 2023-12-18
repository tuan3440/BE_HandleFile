package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysRoleModule;
import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysRoleModuleMapper extends EntityMapper<SysRoleModuleDTO, SysRoleModule> {}
