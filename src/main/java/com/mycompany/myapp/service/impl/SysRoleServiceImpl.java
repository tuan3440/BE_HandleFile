package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.domain.SysRole;
import com.mycompany.myapp.repository.SysRoleRepository;
import com.mycompany.myapp.service.SysRoleService;
import com.mycompany.myapp.service.dto.SysRoleDTO;
import com.mycompany.myapp.service.mapper.SysRoleMapper;
import com.mycompany.myapp.utils.DataUtil;
import com.mycompany.myapp.web.rest.errors.CustomException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleRepository sysRoleRepository;
    private final SysRoleMapper sysRoleMapper;

    public SysRoleServiceImpl(SysRoleRepository sysRoleRepository, SysRoleMapper sysRoleMapper) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public Page<SysRoleDTO> doSearch(String keyword, Integer status, Pageable pageable) {
        if (StringUtils.isNotEmpty(keyword) && StringUtils.isNotBlank(keyword)) {
            keyword = DataUtil.makeLikeQuery(keyword);
        } else {
            keyword = null;
        }
        Page<SysRole> rs = sysRoleRepository.doSearch(keyword, status, pageable);
        List<SysRoleDTO> rsDTO = sysRoleMapper.toDto(rs.getContent());
        return new PageImpl<>(rsDTO, pageable, rs.getTotalElements());
    }

    @Override
    public SysRoleDTO save(SysRoleDTO sysRoleDTO) {
        // check code exist
        if (sysRoleRepository.getSysRoleByCode(sysRoleDTO.getCode()) != null) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "codeRole exist");
        }
        SysRole sysRole = sysRoleMapper.toEntity(sysRoleDTO);
        sysRole = sysRoleRepository.save(sysRole);
        return sysRoleMapper.toDto(sysRole);
    }

    @Override
    public SysRoleDTO update(SysRoleDTO sysRoleDTO) {
        // check code exist
        if (sysRoleRepository.getSysRoleByCodeAndId(sysRoleDTO.getCode(), sysRoleDTO.getId()).size() > 0) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "codeRole exist");
        }
        SysRole sysRole = sysRoleMapper.toEntity(sysRoleDTO);
        sysRole = sysRoleRepository.save(sysRole);
        return sysRoleMapper.toDto(sysRole);
    }

    @Override
    public void delete(Long id) {
        if (sysRoleRepository.findById(id).isEmpty()) {
            throw new CustomException(ResponseCode.CODE.NOT_EXISTED, ResponseCode.MSG.NOT_EXISTED, "sysRole not exist");
        }
        sysRoleRepository.deleteById(id);
    }

    @Override
    public SysRoleDTO findById(Long id) {
        Optional<SysRole> sysRole = sysRoleRepository.findById(id);
        if (sysRole.isEmpty()) {
            throw new CustomException(ResponseCode.CODE.NOT_EXISTED, ResponseCode.MSG.NOT_EXISTED, "sysRole not exist");
        }
        SysRoleDTO result = sysRoleMapper.toDto(sysRole.get());
        return result;
    }
}
