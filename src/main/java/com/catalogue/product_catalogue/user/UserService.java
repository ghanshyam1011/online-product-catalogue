package com.catalogue.product_catalogue.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailTaken(String email) {
        return userRepository.existsByEmail(normalize(email));
    }

    @Transactional
    public User register(String name, String email, String rawPassword) {
        return userRepository.save(
                new User(name.trim(), normalize(email), passwordEncoder.encode(rawPassword), Role.USER));
    }

    private String normalize(String email) {
        return email.trim().toLowerCase();
    }
}