package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysUserRoleDTO;

public interface SysUserRoleService {
    SysUserRoleDTO getRoleByUserId(Long userId);

    void update(Long userId, Long roleId);
}
