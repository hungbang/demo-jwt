package com.aptech.security.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by KAI on 11/14/17.
 */

@Service
public class SMSServiceImpl implements SMSService {

    @Value("${twillio.account.sid}")
    private String accountSID;

    @Value("${twillio.auth.token}")
    private String token;

    @Override
    public void sendSMS(String toNumber, String content) {
        Twilio.init("AC23b21616cae931b2c9e2158deffe297f", "f214cdf5f77a3fe9fc0dad390d0a0ab3");
        Message.creator(new PhoneNumber(toNumber), new PhoneNumber("+19165126554"), content).create();
    }


//    public static void main(String[] args) {
//        String fromNumber = "+19165126554";
//        String toNumber = "+84967583058";
//        String otp = "23242423";
//        SMSServiceImpl smsService = new SMSServiceImpl();
////        smsService.sendSMS(toNumber, fromNumber, otp);
//    }
}
