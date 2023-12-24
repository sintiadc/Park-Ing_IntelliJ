package com.github.parkeer.services;

import com.github.parkeer.entities.admin.AdminEntity;
import com.github.parkeer.repositories.AdminRepository;
import com.github.parkeer.utils.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordUtil passwordUtil;

    public String test() {
        return "ok";
    }

    public AdminEntity login(final String username, final String password) {
        final Optional<AdminEntity> entity = adminRepository.findOneByUsername(username);

        if (entity.isPresent()) {
            final AdminEntity admin = entity.get();
            if (passwordUtil.matches(password, admin.getPassword())) {
                return admin;
            }
        }

        return null;
    }
}
