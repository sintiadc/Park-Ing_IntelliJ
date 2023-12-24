package com.github.parkeer.entities.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Embeddable
public class CustomerId implements Serializable {

    @Column(nullable = false, updatable = false, name = "customer_id")
    private Long customerId;
}
