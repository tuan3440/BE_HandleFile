package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysActionDTO;
import com.mycompany.myapp.service.dto.SysModuleActionDTO;
import java.util.List;

public interface SysModuleActionService {
    List<SysModuleActionDTO> getByModuleId(Long moduleId);

    void deleteAllByModuleId(Long moduleId);

    void saveAll(List<Long> actionIds, Long moduleId);

    List<SysActionDTO> getActionByModuleCode(String code);
}
