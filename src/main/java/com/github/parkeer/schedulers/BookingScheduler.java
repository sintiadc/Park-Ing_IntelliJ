package com.github.parkeer.schedulers;

import com.github.parkeer.entities.booking.BookingEntity;
import com.github.parkeer.enums.BookingStatusType;
import com.github.parkeer.repositories.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class BookingScheduler {

    private final BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000) // run every minute
    public void refreshBookingStatus() {
        final ZonedDateTime oneHourPrior = ZonedDateTime.now().minusHours(1).plusSeconds(1);
        final List<BookingEntity> expiredBookingList = bookingRepository.findAllByStatusAndCreatedAtBefore(BookingStatusType.BOOKED, oneHourPrior);

        if (expiredBookingList.isEmpty()) {
            log.info("no expired booking. skipping refresh status");
            return;
        }

        int count = 0;
        for (BookingEntity booking : expiredBookingList) {
            booking.setStatus(BookingStatusType.CANCELED);
            booking.setUpdatedAt(ZonedDateTime.now());
            bookingRepository.save(booking);

            count += 1;
        }

        log.info("a total of " + count + " expired booking(s) are now set to canceled");
    }
}
