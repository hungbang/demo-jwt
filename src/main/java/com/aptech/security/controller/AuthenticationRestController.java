package com.aptech.security.controller;

import javax.servlet.http.HttpServletRequest;

import com.aptech.security.exception.CustomUnauthorizationException;
import com.aptech.security.exception.OTPGenerateException;
import com.aptech.security.model.LoginResponse;
import com.aptech.security.model.TokenRequestParam;
import com.aptech.security.model.User;
import com.aptech.security.service.OneTimePasswordService;
import com.aptech.security.service.SMSService;
import com.aptech.security.service.UserService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.aptech.security.JwtAuthenticationRequest;
import com.aptech.security.JwtTokenUtil;
import com.aptech.security.JwtUser;
import com.aptech.security.service.JwtAuthenticationResponse;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OneTimePasswordService oneTimePasswordService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);



        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        final String token = jwtTokenUtil.generateToken(userDetails, device);

        User user
                = userService.getUserByUsername(userDetails.getUsername());
        try{
            String OTPCode = oneTimePasswordService.generateOTP(user);
            System.out.println("====OTPCode==="+OTPCode);
            smsService.sendSMS(user.getNumber(), OTPCode);
            LoginResponse loginResponse = new LoginResponse("true", user.getUsername());
            // Return the token
            return ResponseEntity.ok(loginResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody TokenRequestParam requestParam, Device device) throws CustomUnauthorizationException, OTPGenerateException {
        if(requestParam.getOtp() == null || requestParam.getOtp().isEmpty())
            throw new NullPointerException("OTP is missing");

        User userExists = userService.getUserByUsername(requestParam.getUsername());
        String OTPCode = oneTimePasswordService.generateOTP(userExists);

        if(requestParam.getOtp().equalsIgnoreCase(OTPCode)){
            final String token = jwtTokenUtil.generateToken(userExists.getUsername(), device);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
