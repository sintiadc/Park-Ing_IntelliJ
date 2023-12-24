package com.github.parkeer.models.attributes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountLoginModel {

    private String username;
    private String password;
}
