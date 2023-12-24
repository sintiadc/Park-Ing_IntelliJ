package com.github.parkeer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.parkeer.entities.booking.BookingEntity;
import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.entities.vehicle.VehicleEntity;
import com.github.parkeer.enums.AccountRoleType;
import com.github.parkeer.models.CustomerSessionModel;
import com.github.parkeer.models.attributes.BookingCreateModel;
import com.github.parkeer.services.BookingService;
import com.github.parkeer.services.CustomerService;
import com.github.parkeer.services.VehicleService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/create")
    public String create(final HttpSession session, @ModelAttribute final BookingCreateModel bookingCreate) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        if (bookingCreate.getVehicleId() < 1) {
            session.setAttribute("errorMessage", "please choose a vehicle");
            return "redirect:/customer/dashboard";
        }

        final Long availableSpace = bookingService.getAvailableSpace();
        if (availableSpace < 1) {
            session.setAttribute("errorMessage", "no empty space at the moment");
            return "redirect:/customer/dashboard";
        }

        if (bookingService.hasActiveBooking(customerSession.getCustomerId())) {
            session.setAttribute("errorMessage", "you have active booking at the moment");
            return "redirect:/customer/dashboard";
        }

        final BookingEntity booking = bookingService.create(customerSession.getCustomerId(), bookingCreate.getVehicleId());
        if (booking == null) {
            session.setAttribute("errorMessage", "internal server error");
            return "redirect:/customer/dashboard";
        }

        return "redirect:/booking/latest";
    }

    @GetMapping("/latest")
    public String latest(final HttpSession session, final Model model) throws Exception {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        final BookingEntity booking = bookingService.getActiveBooking(customerSession.getCustomerId());
        if (booking == null) {
            session.setAttribute("errorMessage", "you have no active booking. please create one");
            return "redirect:/customer/dashboard";
        }

        final VehicleEntity vehicle = vehicleService.getById(booking.getVehicleId().getVehicleId());
        if (vehicle == null) {
            session.setAttribute("errorMessage", "internal server error. somehow the vehicle is not there");
            return "redirect:/customer/dashboard";
        }

        final String barcode = bookingService.generateCode39BarcodeImage(booking.getBarcode());

        model.addAttribute("title", "Customer Booking");
        model.addAttribute("customer", customerEntity);
        model.addAttribute("booking", booking);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("barcode", barcode);

        return "e-ticket";
    }

    @GetMapping("/history")
    public String history(final HttpSession session, final Model model) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        model.addAttribute("title", "Customer Transaction History");
        model.addAttribute("customer", customerEntity);

        return "History";
    }
}
