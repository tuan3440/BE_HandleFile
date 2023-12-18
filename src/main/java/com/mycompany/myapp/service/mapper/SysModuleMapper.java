package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysModule;
import com.mycompany.myapp.service.dto.SysModuleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysModuleMapper extends EntityMapper<SysModuleDTO, SysModule> {}
