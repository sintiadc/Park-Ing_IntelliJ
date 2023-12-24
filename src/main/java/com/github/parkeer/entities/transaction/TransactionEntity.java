package com.github.parkeer.entities.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.parkeer.entities.base.BaseEntity;
import com.github.parkeer.entities.booking.BookingId;
import com.github.parkeer.entities.employee.EmployeeId;
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
@Table(name = "transaction", schema = "public")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class TransactionEntity extends BaseEntity implements Persistable<TransactionId> {

    @EmbeddedId
    private TransactionId id;

    @Column(nullable = false)
    private BookingId bookingId;

    @Column(nullable = false)
    private EmployeeId employeeId;

    @Column(nullable = false)
    private Double totalFee;

    @Column(nullable = false)
    private String status;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
