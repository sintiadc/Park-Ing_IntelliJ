package com.github.parkeer.models.attributes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleEditModel {

    private Long vehicleId;
    private String prevPoliceNumber;
    private String brand;
    private String type;
    private String color;
    private String policeNumber;
}
