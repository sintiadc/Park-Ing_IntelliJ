package com.github.parkeer.services;

import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.entities.customer.CustomerId;
import com.github.parkeer.models.attributes.CustomerSignupModel;
import com.github.parkeer.repositories.CustomerRepository;
import com.github.parkeer.utils.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordUtil passwordUtil;

    public boolean isUsernameExists(final String username) {
        return customerRepository.existsByUsername(username);
    }

    public CustomerEntity getById(final long customerId) {
        return customerRepository.getReferenceById(new CustomerId(customerId));
    }

    public CustomerEntity handleRegister(final CustomerSignupModel model) {
        final CustomerEntity entity = CustomerEntity.builder()
                .id(new CustomerId(customerRepository.getNextId()))
                .username(model.getUsername())
                .password(passwordUtil.hash(model.getPassword()))
                .fullName(model.getFullName())
                .email(model.getEmail())
                .phoneNumber(model.getPhoneNumber())
                .vehicleList(new ArrayList<>()).build();
        return customerRepository.save(entity);
    }

    public CustomerEntity handleLogin(final String username, final String password) {
        final Optional<CustomerEntity> entity = customerRepository.findOneByUsername(username);

        if (entity.isPresent()) {
            final CustomerEntity customer = entity.get();
            if (passwordUtil.matches(password, customer.getPassword())) {
                return customer;
            }
        }

        return null;
    }
}
