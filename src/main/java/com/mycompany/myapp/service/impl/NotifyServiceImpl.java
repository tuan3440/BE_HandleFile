package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MailServiceCustom;
import com.mycompany.myapp.service.NotifyService;
import com.mycompany.myapp.service.dto.SysUserDTO;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl implements NotifyService {

    private final MailServiceCustom mailServiceCustom;

    public NotifyServiceImpl(MailServiceCustom mailServiceCustom) {
        this.mailServiceCustom = mailServiceCustom;
    }

    @Override
    public void sendCodeForgetPwd(SysUserDTO sysUserDTO, String otpCode) {
        this.mailServiceCustom.sendCodeForgetPwd(sysUserDTO, otpCode);
    }
}
