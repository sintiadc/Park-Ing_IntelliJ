package com.github.parkeer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.entities.vehicle.VehicleEntity;
import com.github.parkeer.enums.AccountRoleType;
import com.github.parkeer.models.CustomerSessionModel;
import com.github.parkeer.models.attributes.VehicleAdditionModel;
import com.github.parkeer.models.attributes.VehicleEditModel;
import com.github.parkeer.services.CustomerService;
import com.github.parkeer.services.VehicleService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping("/vehicle")
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/init")
    public String init(final HttpSession session, final Model model) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customer = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        if (customer.getVehicleNumber() > 0) {
            return "redirect:/customer/dashboard";
        }

        model.addAttribute("title", "Customer Signup - Add Vehicle");
        model.addAttribute("vehicleAddition", new VehicleAdditionModel());

        return "SignUp2";
    }

    @PostMapping("/init")
    public String handleInit(final HttpSession session, final Model model, @ModelAttribute final VehicleAdditionModel vehicleAddition) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customer = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        if (customer.getVehicleNumber() > 0) {
            return "redirect:/customer/dashboard";
        }

        model.addAttribute("title", "Customer Signup - Add Vehicle");
        model.addAttribute("vehicleAddition", vehicleAddition);

        if (vehicleService.isVehicleExists(customer.getCustomerId(), vehicleAddition.getPoliceNumber())) {
            model.addAttribute("errorMessage", "vehicle with this police number already exist");
            return "SignUp2";
        }

        final VehicleEntity vehicle = vehicleService.handleAddVehicle(customer.getCustomerId(), vehicleAddition);
        if (vehicle == null) {
            model.addAttribute("errorMessage", "internal server error");
            return "SignUp2";
        }

        session.removeAttribute(AccountRoleType.CUSTOMER.name());
        session.setAttribute("successMessage", "customer \"" + customer.getUsername() + "\" is now registered");

        return "redirect:/customer/login";
    }

    @GetMapping("/add")
    public String add(final HttpSession session, final Model model) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        model.addAttribute("title", "Add Vehicle");
        model.addAttribute("customer", customerEntity);
        model.addAttribute("vehicleAddition", new VehicleAdditionModel());

        return "addVehicle";
    }

    @PostMapping("/add")
    public String handleAdd(final HttpSession session, final Model model, @ModelAttribute final VehicleAdditionModel vehicleAddition) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        model.addAttribute("title", "Add Vehicle");
        model.addAttribute("customer", customerEntity);
        model.addAttribute("vehicleAddition", vehicleAddition);

        assert customerEntity.getId() != null;
        final long customerId = customerEntity.getId().getCustomerId();
        final String policeNumber = vehicleAddition.getPoliceNumber().replaceAll("\\s", "").toUpperCase(Locale.ENGLISH);

        if (vehicleService.isVehicleExists(customerId, policeNumber)) {
            model.addAttribute("errorMessage", "vehicle with this police number already exist");
            return "addVehicle";
        }

        final VehicleEntity vehicle = vehicleService.handleAddVehicle(customerId, vehicleAddition);
        if (vehicle == null) {
            model.addAttribute("errorMessage", "internal server error");
            return "addVehicle";
        }

        customerSession.setVehicleNumber(customerSession.getVehicleNumber() + 1);
        session.setAttribute(AccountRoleType.CUSTOMER.name(), objectMapper.writeValueAsString(customerSession));

        return "redirect:/customer/dashboard";
    }

    @GetMapping("/{vehicleId}/edit")
    public String edit(@PathVariable final long vehicleId, final HttpSession session, final Model model) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        final VehicleEntity vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null || vehicle.getCustomerId().getCustomerId() != customerEntity.getId().getCustomerId()) {
            return "redirect:/customer/profile";
        }

        model.addAttribute("title", "Edit Vehicle - " + vehicle.getBrand() + " " + vehicle.getType());
        model.addAttribute("customer", customerEntity);
        final VehicleEditModel vehicleEditModel = VehicleEditModel.builder()
                .vehicleId(vehicleId)
                .prevPoliceNumber(vehicle.getPoliceNumber())
                .brand(vehicle.getBrand())
                .type(vehicle.getType())
                .color(vehicle.getColor())
                .policeNumber(vehicle.getPoliceNumber()).build();
        model.addAttribute("vehicleEdit", vehicleEditModel);

        return "editVehicle";
    }

    @PostMapping("/{vehicleId}/edit")
    public String handleEdit(@PathVariable final long vehicleId, final HttpSession session, final Model model, @ModelAttribute final VehicleEditModel vehicleEdit) throws JsonProcessingException {
        final Object account = session.getAttribute(AccountRoleType.CUSTOMER.name());
        if (account == null) {
            return "redirect:/customer/login";
        }

        final CustomerSessionModel customerSession = objectMapper.readValue(account.toString(), CustomerSessionModel.class);
        final CustomerEntity customerEntity = customerService.getById(customerSession.getCustomerId());
        if (customerEntity.getVehicleList().isEmpty()) {
            return "redirect:/vehicle/init";
        }

        final VehicleEntity vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null || vehicle.getCustomerId().getCustomerId() != customerEntity.getId().getCustomerId()) {
            return "redirect:/customer/profile";
        }

        model.addAttribute("title", "Edit Vehicle - " + vehicle.getBrand() + " " + vehicle.getType());
        model.addAttribute("customer", customerEntity);
        model.addAttribute("vehicleEdit", vehicleEdit);

        assert customerEntity.getId() != null;
        final long customerId = customerEntity.getId().getCustomerId();
        final String prevPoliceNumber = vehicleEdit.getPrevPoliceNumber().replaceAll("\\s", "").toUpperCase(Locale.ENGLISH);
        final String policeNumber = vehicleEdit.getPoliceNumber().replaceAll("\\s", "").toUpperCase(Locale.ENGLISH);

        if (!prevPoliceNumber.equals(policeNumber) && vehicleService.isVehicleExists(customerId, policeNumber)) {
            model.addAttribute("errorMessage", "vehicle with this police number already exist");
            return "editVehicle";
        }

        final VehicleEntity updatedVehicle = vehicleService.handleEditVehicle(customerId, vehicleEdit);
        if (updatedVehicle == null) {
            model.addAttribute("errorMessage", "internal server error");
            return "editVehicle";
        }

        model.addAttribute("successMessage", "vehicle updated successfully");

        return "editVehicle";
    }
}
