package com.github.parkeer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.enums.AccountRoleType;
import com.github.parkeer.models.CustomerSessionModel;
import com.github.parkeer.models.attributes.AccountLoginModel;
import com.github.parkeer.models.attributes.BookingCreateModel;
import com.github.parkeer.models.attributes.CustomerSignupModel;
import com.github.parkeer.services.BookingService;
import com.github.parkeer.services.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final BookingService bookingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/signup")
    public String signup(final HttpSession session, final Model model) {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account != null) {
            return "redirect:/customer/dashboard";
        }

        model.addAttribute("title", "Customer Signup");
        model.addAttribute("customerSignup", new CustomerSignupModel());

        return "SignUp";
    }

    @PostMapping("/signup")
    public String handleSignup(final HttpSession session, final Model model, @ModelAttribute CustomerSignupModel customerSignup) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account != null) {
            return "redirect:/customer/dashboard";
        }

        model.addAttribute("title", "Customer Signup");
        model.addAttribute("customerSignup", customerSignup);

        if (customerService.isUsernameExists(customerSignup.getUsername())) {
            model.addAttribute("errorMessage", "username is used by somebody else");
            return "SignUp";
        }

        final CustomerEntity customer = customerService.handleRegister(customerSignup);
        if (customer == null) {
            model.addAttribute("errorMessage", "internal server error");
            return "SignUp";
        }

        assert customer.getId() != null;
        final CustomerSessionModel customerSession = CustomerSessionModel.builder()
                .customerId(customer.getId().getCustomerId())
                .username(customer.getUsername())
                .vehicleNumber(customer.getVehicleList().size()).build();

        session.setAttribute(AccountRoleType.CUSTOMER.name(), objectMapper.writeValueAsString(customerSession));

        return "redirect:/vehicle/init";
    }

    @GetMapping("/login")
    public String login(final HttpSession session, final Model model) {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account != null) {
            return "redirect:/customer/dashboard";
        }

        model.addAttribute("title", "Customer Login");
        model.addAttribute("accountLogin", new AccountLoginModel());
        final Object successMessage = session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage.toString());
            session.removeAttribute("successMessage");
        }

        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(final HttpSession session, final Model model, @ModelAttribute AccountLoginModel accountLogin) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account != null) {
            return "redirect:/customer/dashboard";
        }

        model.addAttribute("title", "Customer Login");
        model.addAttribute("accountLogin", accountLogin);

        final CustomerEntity customer = customerService.handleLogin(accountLogin.getUsername(), accountLogin.getPassword());
        if (customer == null) {
            model.addAttribute("errorMessage", "wrong username or password");
            return "Login";
        }

        assert customer.getId() != null;
        final CustomerSessionModel customerSession = CustomerSessionModel.builder()
                .customerId(customer.getId().getCustomerId())
                .username(customer.getUsername())
                .vehicleNumber(customer.getVehicleList().size()).build();

        session.setAttribute(AccountRoleType.CUSTOMER.name(), objectMapper.writeValueAsString(customerSession));

        if (customer.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        } else {
            return "redirect:/customer/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(final HttpSession session, final Model model) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        model.addAttribute("title", "Customer Dashboard");
        model.addAttribute("customer", customerEntity);
        model.addAttribute("availableSpaceCount", bookingService.getAvailableSpace());
        model.addAttribute("bookingCreate", new BookingCreateModel());

        final Object errorMessage = session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage.toString());
            session.removeAttribute("errorMessage");
        }

        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(final HttpSession session, final Model model) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        model.addAttribute("title", "Customer Profile");
        model.addAttribute("customer", customerEntity);

        return "profil";
    }

    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        session.removeAttribute(AccountRoleType.CUSTOMER.name());

        return "redirect:/";
    }
}
