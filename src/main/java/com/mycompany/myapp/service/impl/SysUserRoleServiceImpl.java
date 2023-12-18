package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SysUserRole;
import com.mycompany.myapp.repository.SysUserRoleRepository;
import com.mycompany.myapp.service.SysUserRoleService;
import com.mycompany.myapp.service.dto.SysUserRoleDTO;
import com.mycompany.myapp.service.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserRoleServiceImpl implements SysUserRoleService {

    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysUserRoleMapper sysUserRoleMapper;

    public SysUserRoleServiceImpl(SysUserRoleRepository sysUserRoleRepository, SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Override
    public SysUserRoleDTO getRoleByUserId(Long userId) {
        SysUserRole rs = this.sysUserRoleRepository.findSysUserRoleByUserId(userId);
        return this.sysUserRoleMapper.toDto(rs);
    }

    @Override
    public void update(Long userId, Long roleId) {
        SysUserRole sysUserRole = this.sysUserRoleRepository.findSysUserRoleByUserId(userId);
        if (sysUserRole != null) {
            sysUserRole.setRoleId(roleId);
            this.sysUserRoleRepository.save(sysUserRole);
        } else {
            SysUserRole sysUserRoleTemp = new SysUserRole();
            sysUserRoleTemp.setRoleId(roleId);
            sysUserRoleTemp.setUserId(userId);
            this.sysUserRoleRepository.save(sysUserRoleTemp);
        }
    }
}
