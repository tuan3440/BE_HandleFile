package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ChangePwDTO;
import com.mycompany.myapp.service.dto.SysUserDTO;
import com.mycompany.myapp.web.rest.vm.LoginVM;
import java.io.UnsupportedEncodingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SysUserService {
    void validateCaptcha(LoginVM loginVM);
    SysUserDTO findByEmail(String email);

    SysUserDTO findByUserName(String userName);

    String createHashKey(SysUserDTO sysUserDTO) throws UnsupportedEncodingException;

    void resetPassword(ChangePwDTO changePwdDTO);

    Page<SysUserDTO> doSearch(String keyword, Integer status, Pageable pageable);
}
