package com.github.parkeer.models.attributes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerSignupModel {

    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
}
