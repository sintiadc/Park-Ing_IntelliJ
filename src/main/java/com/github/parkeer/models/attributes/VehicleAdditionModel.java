package com.github.parkeer.models.attributes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleAdditionModel {

    private String brand;
    private String type;
    private String color;
    private String policeNumber;
}
