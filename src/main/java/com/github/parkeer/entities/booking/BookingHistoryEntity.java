package com.github.parkeer.entities.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.parkeer.entities.base.BaseEntity;
import com.github.parkeer.enums.BookingStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import java.time.ZonedDateTime;

@Entity
@Table(name = "booking_history", schema = "public")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class BookingHistoryEntity extends BaseEntity implements Persistable<BookingHistoryId> {

    @EmbeddedId
    private BookingHistoryId id;

    @Column(nullable = false)
    private BookingId bookingId;

    @Column(nullable = false)
    private ZonedDateTime checkpoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatusType status;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
