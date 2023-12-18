package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SysRole;
import com.mycompany.myapp.domain.SysUser;
import com.mycompany.myapp.service.dto.SysRoleDTO;
import com.mycompany.myapp.service.dto.SysUserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SysUserMapper extends EntityMapper<SysUserDTO, SysUser> {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleRoleCodeSet")
    SysUserDTO toDto(SysUser s);

    @Named("roleRoleCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    SysRoleDTO toDtoRoleRoleCode(SysRole role);

    @Named("roleRoleCodeSet")
    default Set<SysRoleDTO> toDtoRoleRoleNameSet(Set<SysRole> role) {
        return role.stream().map(this::toDtoRoleRoleCode).collect(Collectors.toSet());
    }
}
