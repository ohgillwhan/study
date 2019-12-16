package com.example.study.redis.user.service;

import java.util.List;

public interface UserService {
    User save(User user);
    List<User> saveAll(List<User> user);
}
