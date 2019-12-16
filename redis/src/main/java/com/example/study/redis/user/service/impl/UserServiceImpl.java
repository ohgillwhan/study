package com.example.study.redis.user.service.impl;

import com.example.study.redis.user.service.User;
import com.example.study.redis.user.service.UserRepository;
import com.example.study.redis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public List<User> saveAll(List<User> user) {
        userRepository.saveAll(user);
        return  user;
    }
}
