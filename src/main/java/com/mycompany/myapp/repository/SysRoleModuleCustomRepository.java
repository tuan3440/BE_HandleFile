package com.mycompany.myapp.repository;

import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import java.util.List;

public interface SysRoleModuleCustomRepository {
    List<SysRoleModuleDTO> findByUserId(Long id);
}
