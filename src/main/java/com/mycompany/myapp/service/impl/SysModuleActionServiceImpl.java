package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SysAction;
import com.mycompany.myapp.domain.SysModuleAction;
import com.mycompany.myapp.repository.SysModuleActionRepository;
import com.mycompany.myapp.service.SysModuleActionService;
import com.mycompany.myapp.service.dto.SysActionDTO;
import com.mycompany.myapp.service.dto.SysModuleActionDTO;
import com.mycompany.myapp.service.mapper.SysActionMapper;
import com.mycompany.myapp.service.mapper.SysModuleActionMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysModuleActionServiceImpl implements SysModuleActionService {

    private final SysModuleActionRepository sysModuleActionRepository;
    private final SysModuleActionMapper sysModuleActionMapper;
    private final SysActionMapper sysActionMapper;

    public SysModuleActionServiceImpl(
        SysModuleActionRepository sysModuleActionRepository,
        SysModuleActionMapper sysModuleActionMapper,
        SysActionMapper sysActionMapper
    ) {
        this.sysModuleActionRepository = sysModuleActionRepository;
        this.sysModuleActionMapper = sysModuleActionMapper;
        this.sysActionMapper = sysActionMapper;
    }

    @Override
    public List<SysModuleActionDTO> getByModuleId(Long moduleId) {
        List<SysModuleAction> rs = sysModuleActionRepository.findByModuleId(moduleId);
        return sysModuleActionMapper.toDto(rs);
    }

    @Override
    public void deleteAllByModuleId(Long moduleId) {
        sysModuleActionRepository.deleteAllByModuleId(moduleId);
    }

    @Override
    public void saveAll(List<Long> actionIds, Long moduleId) {
        for (Long actionId : actionIds) {
            SysModuleAction sysModuleAction = new SysModuleAction();
            sysModuleAction.setActionId(actionId);
            sysModuleAction.setModuleId(moduleId);
            sysModuleActionRepository.save(sysModuleAction);
        }
    }

    @Override
    public List<SysActionDTO> getActionByModuleCode(String code) {
        List<SysAction> rs = sysModuleActionRepository.getListAction(code);
        return sysActionMapper.toDto(rs);
    }
}
