package com.github.parkeer.entities.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.parkeer.entities.base.BaseEntity;
import com.github.parkeer.entities.customer.CustomerId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "vehicle", schema = "public")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class VehicleEntity extends BaseEntity implements Persistable<VehicleId> {

    @EmbeddedId
    private VehicleId id;

    @Column(nullable = false)
    private CustomerId customerId;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String policeNumber;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
