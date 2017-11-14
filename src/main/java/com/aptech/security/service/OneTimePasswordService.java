package com.aptech.security.service;

import com.aptech.security.exception.OTPGenerateException;
import com.aptech.security.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by KAI on 11/14/17.
 */
public interface OneTimePasswordService {
    String generateOTP(User userDetails) throws OTPGenerateException;
}
