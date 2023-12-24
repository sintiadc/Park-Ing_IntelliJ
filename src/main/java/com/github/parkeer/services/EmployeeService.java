package com.github.parkeer.services;

import com.github.parkeer.entities.employee.EmployeeEntity;
import com.github.parkeer.repositories.EmployeeRepository;
import com.github.parkeer.utils.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordUtil passwordUtil;

    public String test() {
        return "ok";
    }

    public EmployeeEntity login(final String username, final String password) {
        final Optional<EmployeeEntity> entity = employeeRepository.findOneByUsername(username);

        if (entity.isPresent()) {
            final EmployeeEntity employee = entity.get();
            if (passwordUtil.matches(password, employee.getPassword())) {
                return employee;
            }
        }

        return null;
    }
}
