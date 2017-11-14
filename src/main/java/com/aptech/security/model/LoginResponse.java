package com.aptech.security.model;

import java.util.Objects;
import java.util.StringJoiner;


public class LoginResponse {

    String otpRequired;

    String username;

    public String getUsername() {
        return username;
    }

    public LoginResponse setUsername(String username) {
        this.username = username;
        return this;
    }


    public String getOtpRequired() {
        return otpRequired;
    }

    public LoginResponse setOtpRequired(String otpRequired) {
        this.otpRequired = otpRequired;
        return this;
    }

    public LoginResponse(String otpRequired, String username) {
        this.otpRequired = otpRequired;
        this.username = username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginResponse that = (LoginResponse) o;

        return Objects.equals(this.otpRequired, that.otpRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(otpRequired);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("otpRequired = " + otpRequired)
                .toString();
    }
}
