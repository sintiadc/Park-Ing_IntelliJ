package com.github.parkeer.controllers;

import com.github.parkeer.services.BookingHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/booking-history")
@AllArgsConstructor
public class BookingHistoryController {

    private final BookingHistoryService bookingHistoryService;

    @GetMapping(value = "/test", produces = "text/plain")
    @ResponseBody
    public String test() {
        return bookingHistoryService.test();
    }
}
