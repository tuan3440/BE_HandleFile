package com.mycompany.myapp.service.dto;

public class ChangePwDTO {

    private String email;
    private String newPass;
    private String comPass;
    private String userCaptcha;
    private String code;
    private String key;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getComPass() {
        return comPass;
    }

    public void setComPass(String comPass) {
        this.comPass = comPass;
    }

    public String getUserCaptcha() {
        return userCaptcha;
    }

    public void setUserCaptcha(String userCaptcha) {
        this.userCaptcha = userCaptcha;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
