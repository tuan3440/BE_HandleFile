package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.domain.SysAction;
import com.mycompany.myapp.repository.SysActionRepository;
import com.mycompany.myapp.service.SysActionService;
import com.mycompany.myapp.service.dto.SysActionDTO;
import com.mycompany.myapp.service.mapper.SysActionMapper;
import com.mycompany.myapp.utils.DataUtil;
import com.mycompany.myapp.web.rest.errors.CustomException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SysActionServiceImpl implements SysActionService {

    private final SysActionRepository sysActionRepository;
    private final SysActionMapper sysActionMapper;

    public SysActionServiceImpl(SysActionRepository sysActionRepository, SysActionMapper sysActionMapper) {
        this.sysActionRepository = sysActionRepository;
        this.sysActionMapper = sysActionMapper;
    }

    @Override
    public Page<SysActionDTO> doSearch(String keyword, Integer status, Pageable pageable) {
        if (StringUtils.isNotEmpty(keyword) && StringUtils.isNotBlank(keyword)) {
            keyword = DataUtil.makeLikeQuery(keyword);
        } else {
            keyword = null;
        }
        Page<SysAction> rs = sysActionRepository.doSearch(keyword, status, pageable);
        List<SysActionDTO> rsDTO = sysActionMapper.toDto(rs.getContent());
        return new PageImpl<>(rsDTO, pageable, rs.getTotalElements());
    }

    @Override
    public SysActionDTO save(SysActionDTO sysActionDTO) {
        // check code exist
        if (sysActionRepository.existsByCode(sysActionDTO.getCode())) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "codeRole exist");
        }
        SysAction sysAction = sysActionMapper.toEntity(sysActionDTO);
        sysAction = sysActionRepository.save(sysAction);
        return sysActionMapper.toDto(sysAction);
    }

    @Override
    public SysActionDTO update(SysActionDTO sysActionDTO) {
        // check code exist
        if (sysActionRepository.existsByCodeAndIdNot(sysActionDTO.getCode(), sysActionDTO.getId())) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "codeRole exist");
        }
        SysAction sysAction = sysActionMapper.toEntity(sysActionDTO);
        sysAction = sysActionRepository.save(sysAction);
        return sysActionMapper.toDto(sysAction);
    }

    @Override
    public void delete(Long id) {
        if (sysActionRepository.findById(id).isEmpty()) {
            throw new CustomException(ResponseCode.CODE.NOT_EXISTED, ResponseCode.MSG.NOT_EXISTED, "sysRole not exist");
        }
        sysActionRepository.deleteById(id);
    }
}
