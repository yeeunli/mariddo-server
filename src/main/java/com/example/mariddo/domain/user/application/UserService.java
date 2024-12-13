package com.example.mariddo.domain.user.application;

import com.example.mariddo.domain.user.dao.UserRepository;
import com.example.mariddo.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("unexpected user"));
    }
}

