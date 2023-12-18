package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysUserDTO;

public interface NotifyService {
    void sendCodeForgetPwd(SysUserDTO sysUserDTO, String otpCode);
}
