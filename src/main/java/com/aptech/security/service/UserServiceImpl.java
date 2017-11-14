package com.aptech.security.service;

import com.aptech.security.model.User;
import com.aptech.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
