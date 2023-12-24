package com.github.parkeer.repositories;

import com.github.parkeer.entities.customer.CustomerId;
import com.github.parkeer.entities.vehicle.VehicleEntity;
import com.github.parkeer.entities.vehicle.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, VehicleId> {

    @Query(value = "SELECT NEXTVAL('public.vehicle_vehicle_id_seq')", nativeQuery = true)
    Long getNextId();

    boolean existsByCustomerIdAndPoliceNumber(final CustomerId customerId, final String policeNumber);
}
