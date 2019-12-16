package com.example.study.redis.user.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@RedisHash("user")
@Getter @Setter
public class User {
    @Id
    private String id;
    private List<String> test;
}
