package com.mycompany.myapp.service;

import java.util.Map;

public interface OTPService {
    public String createCodeByEmail(String email);

    public Map<String, Object> verifyOtp(String email, String otpCode);
}
