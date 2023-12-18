package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysModuleAction;
import com.mycompany.myapp.service.dto.SysModuleActionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysModuleActionMapper extends EntityMapper<SysModuleActionDTO, SysModuleAction> {}
