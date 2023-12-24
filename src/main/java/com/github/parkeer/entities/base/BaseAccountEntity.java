package com.github.parkeer.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@MappedSuperclass
public class BaseAccountEntity extends BaseEntity {

    @Column(nullable = false)
    protected String username;

    @Column(nullable = false)
    protected String password;

    @Column
    protected String fullName;
}
