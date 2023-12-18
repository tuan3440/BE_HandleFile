package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SysUserDTO;

public interface MailServiceCustom {
    void sendCodeForgetPwd(SysUserDTO sysUserDTO, String otpCode);
}
