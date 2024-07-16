package com.alurachallenges.forohub.services;

import org.springframework.stereotype.Service;

import com.alurachallenges.forohub.models.User;

@Service
public interface UserService {

    User save(User user);

    boolean existsByUsername(String username);

}