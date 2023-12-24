package com.github.parkeer.entities.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Embeddable
public class EmployeeId {

    @Column(nullable = false, updatable = false)
    private Long employeeId;
}
