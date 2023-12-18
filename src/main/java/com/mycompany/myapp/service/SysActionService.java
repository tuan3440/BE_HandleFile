package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysActionDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SysActionService {
    Page<SysActionDTO> doSearch(String keyword, Integer status, Pageable pageable);

    SysActionDTO save(SysActionDTO sysActionDTO);

    SysActionDTO update(SysActionDTO sysActionDTO);

    void delete(Long id);
}
