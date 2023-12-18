package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ModuleTree;
import com.mycompany.myapp.service.dto.SysModuleDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SysModuleService {
    Page<SysModuleDTO> doSearch(String keyword, Integer status, Long parentId, Pageable pageable);

    SysModuleDTO save(SysModuleDTO sysModuleDTO);

    SysModuleDTO update(SysModuleDTO sysModuleDTO);

    void deleteById(Long id);

    List<ModuleTree> getTree();

    SysModuleDTO findById(Long id);

    List<SysModuleDTO> getMenuByUserId(Long userId);
}
