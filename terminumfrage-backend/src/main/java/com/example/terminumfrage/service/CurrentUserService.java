package com.example.terminumfrage.service;

import com.example.terminumfrage.data.UserRepository;
import com.example.terminumfrage.model.User;
import jakarta.validation.ValidationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ValidationException("Authentication missing");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ValidationException("User not found"));
    }
}
