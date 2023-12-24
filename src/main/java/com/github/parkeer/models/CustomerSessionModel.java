package com.github.parkeer.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerSessionModel {

    private Long customerId;
    private String username;
    private Integer vehicleNumber;
}
