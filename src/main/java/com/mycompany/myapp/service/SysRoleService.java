package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SysRoleService {
    Page<SysRoleDTO> doSearch(String keyword, Integer status, Pageable pageable);

    SysRoleDTO save(SysRoleDTO sysRoleDTO);
    SysRoleDTO update(SysRoleDTO sysRoleDTO);

    void delete(Long id);

    SysRoleDTO findById(Long id);
}
