package com.catalogue.product_catalogue.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:admin@catalogue.local}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${USER_EMAIL:user@catalogue.local}")
    private String userEmail;

    @Value("${USER_PASSWORD:}")
    private String userPassword;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seed("Admin", adminEmail, adminPassword, Role.ADMIN);
        seed("Demo User", userEmail, userPassword, Role.USER);
    }

    private void seed(String name, String email, String password, Role role) {
        if (password.isBlank()) {
            log.warn("Skipping {} account: password environment variable is not set", role);
            return;
        }
        if (userRepository.existsByEmail(email)) {
            return;
        }
        userRepository.save(new User(name, email, passwordEncoder.encode(password), role));
        log.info("Created {} account {}", role, email);
    }
}