package com.github.parkeer.entities.booking;

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
public class BookingId {

    @Column(nullable = false, updatable = false, name = "booking_id")
    private Long bookingId;
}
