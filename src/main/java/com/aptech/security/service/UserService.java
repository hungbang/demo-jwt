package com.aptech.security.service;

import com.aptech.security.model.User;

/**
 * Created by KAI on 11/14/17.
 */
public interface UserService {
    User getUserByUsername(String username);
}
