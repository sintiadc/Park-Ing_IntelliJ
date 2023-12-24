package com.github.parkeer.entities.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.parkeer.entities.base.BaseEntity;
import com.github.parkeer.entities.customer.CustomerId;
import com.github.parkeer.entities.transaction.TransactionEntity;
import com.github.parkeer.entities.vehicle.VehicleId;
import com.github.parkeer.enums.BookingStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Entity
@Table(name = "booking", schema = "public")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class BookingEntity extends BaseEntity implements Persistable<BookingId> {

    @EmbeddedId
    private BookingId id;

    @Column(nullable = false)
    private CustomerId customerId;

    @Column(nullable = false)
    private VehicleId vehicleId;

    @OneToMany
    @JoinColumn(name = "booking_id")
    private List<BookingHistoryEntity> bookingHistoryList;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private TransactionEntity transaction;

    @Column(nullable = false)
    private Double baseFee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatusType status;

    @Column(nullable = false)
    private String barcode;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
