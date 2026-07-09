package com.stase.services;

import org.springframework.stereotype.Service;

import com.stase.dtos.user.UserDto;
import com.stase.entities.User;
import com.stase.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getUsername(), user.getEmail(), null);
    }
}
