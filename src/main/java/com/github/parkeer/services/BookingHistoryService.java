package com.github.parkeer.services;

import com.github.parkeer.repositories.BookingHistoryRepository;
import com.github.parkeer.repositories.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class BookingHistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;
    private final BookingRepository bookingRepository;

    public String test() {
        return "ok";
    }
}
