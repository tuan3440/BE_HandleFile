package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.domain.ModuleTree;
import com.mycompany.myapp.domain.SysModule;
import com.mycompany.myapp.repository.SysModuleRepository;
import com.mycompany.myapp.service.SysModuleService;
import com.mycompany.myapp.service.dto.SysModuleDTO;
import com.mycompany.myapp.service.mapper.SysModuleMapper;
import com.mycompany.myapp.utils.DataUtil;
import com.mycompany.myapp.web.rest.errors.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SysModuleServiceImpl implements SysModuleService {

    private final SysModuleRepository sysModuleRepository;
    private final SysModuleMapper sysModuleMapper;

    public SysModuleServiceImpl(SysModuleRepository sysModuleRepository, SysModuleMapper sysModuleMapper) {
        this.sysModuleRepository = sysModuleRepository;
        this.sysModuleMapper = sysModuleMapper;
    }

    @Override
    public Page<SysModuleDTO> doSearch(String keyword, Integer status, Long parentId, Pageable pageable) {
        if (StringUtils.isNotEmpty(keyword) && StringUtils.isNotBlank(keyword)) {
            keyword = DataUtil.makeLikeQuery(keyword);
        } else {
            keyword = null;
        }
        Page<SysModule> rs = sysModuleRepository.doSearch(keyword, status, parentId, pageable);
        List<SysModuleDTO> rsDTO = sysModuleMapper.toDto(rs.getContent());
        return new PageImpl<>(rsDTO, pageable, rs.getTotalElements());
    }

    @Override
    public SysModuleDTO save(SysModuleDTO sysModuleDTO) {
        // check code exist
        if (sysModuleRepository.existsByCode(sysModuleDTO.getCode())) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "sysmodule code exist");
        }
        SysModule sysModule = sysModuleMapper.toEntity(sysModuleDTO);
        sysModule = sysModuleRepository.save(sysModule);
        return sysModuleMapper.toDto(sysModule);
    }

    @Override
    public SysModuleDTO update(SysModuleDTO sysModuleDTO) {
        //check code exist
        if (sysModuleRepository.existsByCodeAndIdNot(sysModuleDTO.getCode(), sysModuleDTO.getId())) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "module code exist");
        }
        SysModule sysModule = sysModuleMapper.toEntity(sysModuleDTO);
        sysModule = sysModuleRepository.save(sysModule);
        return sysModuleMapper.toDto(sysModule);
    }

    @Override
    public void deleteById(Long id) {
        Optional<SysModule> sysModule = sysModuleRepository.findById(id);
        // check exist
        if (sysModule.isEmpty()) {
            throw new CustomException(ResponseCode.CODE.NOT_EXISTED, ResponseCode.MSG.NOT_EXISTED, "module not exist");
        } else {
            // check has child module
            if (sysModuleRepository.existsByParentIdAndStatus(sysModule.get().getId(), 1)) {
                throw new CustomException(ResponseCode.CODE.UNABLE_TO_DELETE, ResponseCode.MSG.UNABLE_TO_DELETE, "module has child module");
            } else {
                sysModuleRepository.deleteById(id);
            }
        }
    }

    @Override
    public List<ModuleTree> getTree() {
        List<ModuleTree> result = new ArrayList<>();
        result = this.getTreeList(result, -1l);
        return result;
    }

    @Override
    public SysModuleDTO findById(Long id) {
        Optional<SysModule> sysModule = sysModuleRepository.findById(id);
        return sysModuleMapper.toDto(sysModule.get());
    }

    @Override
    public List<SysModuleDTO> getMenuByUserId(Long userId) {
        List<SysModule> data = sysModuleRepository.getMenuByUserId(userId);
        return sysModuleMapper.toDto(data);
    }

    public List<ModuleTree> getTreeList(List<ModuleTree> moduleTreeList, Long parentId) {
        List<SysModule> list = sysModuleRepository.getChildModule(parentId);
        for (SysModule sysModule : list) {
            ModuleTree temp = new ModuleTree();
            temp.setName(sysModule.getName());
            temp.setId(sysModule.getId());
            temp.setStatus(sysModule.getStatus());
            temp.setCode(sysModule.getCode());
            temp.setChildren(this.getTreeList(new ArrayList<>(), sysModule.getId()));
            moduleTreeList.add(temp);
        }
        return moduleTreeList;
    }
}
