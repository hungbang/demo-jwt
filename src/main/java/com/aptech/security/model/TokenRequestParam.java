package com.aptech.security.model;

/**
 * Created by KAI on 11/14/17.
 */
public class TokenRequestParam {
    private String otp;
    private String username;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
