package com.github.parkeer.repositories;

import com.github.parkeer.entities.booking.BookingHistoryEntity;
import com.github.parkeer.entities.booking.BookingHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistoryEntity, BookingHistoryId> {

    @Query(value = "SELECT NEXTVAL('public.booking_history_booking_history_id_seq')", nativeQuery = true)
    Long getNextId();
}
