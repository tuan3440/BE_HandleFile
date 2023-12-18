package com.mycompany.myapp.service.impl;

import com.google.gson.Gson;
import com.mycompany.myapp.config.ApplicationContextHolder;
import com.mycompany.myapp.service.OTPService;
import com.mycompany.myapp.service.form.OtpForm;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OTPServiceImpl implements OTPService {

    private final RedisTemplate<Object, Object> redisTemplate = (RedisTemplate<Object, Object>) ApplicationContextHolder
        .getContext()
        .getBean("redisTemplate");
    private HashOperations<Object, Object, Object> hashOperations;

    @Value("${spring.otp-expired-time:300000}")
    private long otpExpiredTime;

    public static final int OTP_LENGTH = 6;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public String createCodeByEmail(String email) {
        Object otpJson = hashOperations.get("otp", email);
        OtpForm otpForm = new OtpForm();
        if (otpJson != null) {
            try {
                otpForm = new Gson().fromJson(otpJson.toString(), OtpForm.class);
            } catch (Exception ex) {}
        }
        long currentTimeMillis = System.currentTimeMillis();
        long expiredTime = otpForm.getCreatedTime() + otpExpiredTime;
        String otpCode = getRandomNumber(OTP_LENGTH);
        otpForm.setOtpCode(otpCode);
        otpForm.setCreatedTime(currentTimeMillis);
        otpForm.setExpiredTime(expiredTime);
        hashOperations.put("otp", email, new Gson().toJson(otpForm));
        return otpCode;
    }

    private String getRandomNumber(long number) {
        StringBuilder val = new StringBuilder();
        for (long i = 0; i < number; i++) {
            Random rand = new Random();
            int n = rand.nextInt(10);
            val.append(n);
        }
        return val.toString();
    }

    @Override
    public Map<String, Object> verifyOtp(String email, String otpCode) {
        Map<String, Object> result = new HashMap<>();
        String otpJson = String.valueOf(hashOperations.get("otp", email));
        OtpForm otpForm;
        if (StringUtils.hasLength(otpJson)) {
            otpForm = new Gson().fromJson(otpJson, OtpForm.class);
        } else {
            otpForm = new OtpForm();
        }
        if (otpForm == null || !otpForm.getOtpCode().equals(otpCode)) {
            result.put("isSuccess", false);
            result.put("message", "Sai mã OTP_CODE");
            return result;
        }
        long createdTime = otpForm.getCreatedTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (createdTime + otpExpiredTime < currentTimeMillis) {
            hashOperations.delete("otp", email);
            result.put("isSuccess", false);
            result.put("message", "OTP_CODE hết hạn");
            return result;
        }
        hashOperations.delete("otp", email);
        result.put("isSuccess", true);
        result.put("message", "OTP_CODE hợp lệ");
        return result;
    }
}
