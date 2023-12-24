package com.github.parkeer.entities.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.parkeer.entities.base.BaseAccountEntity;
import com.github.parkeer.entities.vehicle.VehicleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Entity
@Table(name = "customer", schema = "public")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class CustomerEntity extends BaseAccountEntity implements Persistable<CustomerId> {

    @EmbeddedId
    private CustomerId id;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private List<VehicleEntity> vehicleList;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
