package com.github.parkeer.services;

import com.github.parkeer.repositories.BookingRepository;
import com.github.parkeer.repositories.EmployeeRepository;
import com.github.parkeer.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;

    public String test() {
        return "ok";
    }
}
