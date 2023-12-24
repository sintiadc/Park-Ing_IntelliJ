package com.github.parkeer.repositories;

import com.github.parkeer.entities.booking.BookingEntity;
import com.github.parkeer.entities.booking.BookingId;
import com.github.parkeer.entities.customer.CustomerId;
import com.github.parkeer.enums.BookingStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, BookingId> {

    @Query(value = "SELECT NEXTVAL('public.booking_booking_id_seq')", nativeQuery = true)
    Long getNextId();

    Long countByStatusIn(Collection<BookingStatusType> bookingStatuses);

    Long countByCustomerIdAndStatusIn(CustomerId customerId, Collection<BookingStatusType> bookingStatuses);

    List<BookingEntity> findAllByStatusAndCreatedAtBefore(BookingStatusType bookingStatus, ZonedDateTime createdAt);

    Optional<BookingEntity> findOneByCustomerIdAndStatusIn(CustomerId customerId, Collection<BookingStatusType> bookingStatuses);
}
