package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SysRoleModule;
import com.mycompany.myapp.repository.SysRoleModuleCustomRepository;
import com.mycompany.myapp.repository.SysRoleModuleRepository;
import com.mycompany.myapp.service.SysRoleModuleService;
import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import com.mycompany.myapp.service.mapper.SysRoleModuleMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysRoleModuleServiceImpl implements SysRoleModuleService {

    private final SysRoleModuleRepository sysRoleModuleRepository;
    private final SysRoleModuleMapper sysRoleModuleMapper;

    private final SysRoleModuleCustomRepository sysRoleModuleCustomRepository;

    public SysRoleModuleServiceImpl(
        SysRoleModuleRepository sysRoleModuleRepository,
        SysRoleModuleMapper sysRoleModuleMapper,
        SysRoleModuleCustomRepository sysRoleModuleCustomRepository
    ) {
        this.sysRoleModuleRepository = sysRoleModuleRepository;
        this.sysRoleModuleMapper = sysRoleModuleMapper;
        this.sysRoleModuleCustomRepository = sysRoleModuleCustomRepository;
    }

    @Override
    public void deleteAllByRoleId(Long roleId) {
        this.sysRoleModuleRepository.deleteAllByRoleId(roleId);
    }

    @Override
    public void save(SysRoleModuleDTO item) {
        SysRoleModule sysRoleModule = sysRoleModuleMapper.toEntity(item);
        this.sysRoleModuleRepository.save(sysRoleModule);
    }

    @Override
    public List<SysRoleModuleDTO> getRoleModuleByUserId(Long id) {
        return this.sysRoleModuleCustomRepository.findByUserId(id);
    }
}
