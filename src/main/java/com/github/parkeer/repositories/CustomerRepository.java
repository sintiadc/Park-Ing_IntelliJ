package com.github.parkeer.repositories;

import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.entities.customer.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, CustomerId> {

    @Query(value = "SELECT NEXTVAL('public.customer_customer_id_seq')", nativeQuery = true)
    Long getNextId();

    Optional<CustomerEntity> findOneByUsername(final String username);

    boolean existsByUsername(final String username);
}
