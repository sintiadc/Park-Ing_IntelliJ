package com.github.parkeer.models.attributes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookingCreateModel {

    private Long vehicleId;
}
