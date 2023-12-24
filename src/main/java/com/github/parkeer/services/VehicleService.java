package com.github.parkeer.services;

import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.entities.customer.CustomerId;
import com.github.parkeer.entities.vehicle.VehicleEntity;
import com.github.parkeer.entities.vehicle.VehicleId;
import com.github.parkeer.models.attributes.VehicleAdditionModel;
import com.github.parkeer.models.attributes.VehicleEditModel;
import com.github.parkeer.repositories.CustomerRepository;
import com.github.parkeer.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public boolean isVehicleExists(final long customerId, final String policeNumber) {
        return vehicleRepository.existsByCustomerIdAndPoliceNumber(new CustomerId(customerId), policeNumber);
    }

    public VehicleEntity handleAddVehicle(final long customerId, final VehicleAdditionModel model) {
        final CustomerEntity customer = customerRepository.findById(new CustomerId(customerId))
                .orElseThrow(() -> new IllegalStateException("customer id " + customerId + " not found"));
        final String policeNumber = model.getPoliceNumber().replaceAll("\\s", "").toUpperCase(Locale.ENGLISH);
        final VehicleEntity vehicle = VehicleEntity.builder()
                .id(new VehicleId(vehicleRepository.getNextId()))
                .customerId(customer.getId())
                .brand(model.getBrand())
                .type(model.getType())
                .color(model.getColor())
                .policeNumber(policeNumber).build();

        return vehicleRepository.save(vehicle);
    }

    public VehicleEntity getById(final long vehicleId) {
        return vehicleRepository.findById(new VehicleId(vehicleId)).orElse(null);
    }

    @Transactional
    public VehicleEntity handleEditVehicle(final long customerId, final VehicleEditModel model) {
        final CustomerEntity customer = customerRepository.findById(new CustomerId(customerId))
                .orElseThrow(() -> new IllegalStateException("customer id " + customerId + " not found"));
        final VehicleEntity vehicle = vehicleRepository.findById(new VehicleId(model.getVehicleId()))
                .orElseThrow(() -> new IllegalStateException("vehicle id " + model.getVehicleId() + " not found"));
        final String prevPoliceNumber = model.getPrevPoliceNumber().replaceAll("\\s", "").toUpperCase(Locale.ENGLISH);

        if (model.getPoliceNumber().equals(prevPoliceNumber)) {
            vehicle.setBrand(model.getBrand());
            vehicle.setType(model.getType());
            vehicle.setColor(model.getColor());

            return vehicleRepository.save(vehicle);
        } else {
            vehicleRepository.delete(vehicle);

            final String policeNumber = model.getPoliceNumber().replaceAll("\\s", "").toUpperCase(Locale.ENGLISH);
            final VehicleEntity updatedVehicle = VehicleEntity.builder()
                    .id(new VehicleId(vehicleRepository.getNextId()))
                    .customerId(customer.getId())
                    .brand(model.getBrand())
                    .type(model.getType())
                    .color(model.getColor())
                    .policeNumber(policeNumber).build();

            return vehicleRepository.save(updatedVehicle);
        }
    }
}
