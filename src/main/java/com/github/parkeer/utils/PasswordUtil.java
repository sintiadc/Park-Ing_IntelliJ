package com.github.parkeer.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PasswordUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String hash(final String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(final String rawPassword, final String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
