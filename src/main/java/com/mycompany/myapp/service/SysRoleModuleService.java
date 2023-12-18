package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import java.util.List;

public interface SysRoleModuleService {
    void deleteAllByRoleId(Long roleId);

    void save(SysRoleModuleDTO item);

    List<SysRoleModuleDTO> getRoleModuleByUserId(Long id);
}
