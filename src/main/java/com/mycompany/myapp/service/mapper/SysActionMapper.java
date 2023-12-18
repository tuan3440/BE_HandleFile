package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysAction;
import com.mycompany.myapp.service.dto.SysActionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface SysActionMapper extends EntityMapper<SysActionDTO, SysAction> {}
