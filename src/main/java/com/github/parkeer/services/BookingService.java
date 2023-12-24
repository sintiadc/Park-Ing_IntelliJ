package com.github.parkeer.services;

import com.github.parkeer.configs.ApplicationConfig;
import com.github.parkeer.entities.booking.BookingEntity;
import com.github.parkeer.entities.booking.BookingId;
import com.github.parkeer.entities.customer.CustomerEntity;
import com.github.parkeer.entities.customer.CustomerId;
import com.github.parkeer.entities.vehicle.VehicleEntity;
import com.github.parkeer.entities.vehicle.VehicleId;
import com.github.parkeer.enums.BookingStatusType;
import com.github.parkeer.repositories.BookingRepository;
import com.github.parkeer.repositories.CustomerRepository;
import com.github.parkeer.repositories.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final ApplicationConfig applicationConfig;

    public Long getAvailableSpace() {
        final Long usedSpaceCount = bookingRepository.countByStatusIn(Set.of(BookingStatusType.BOOKED, BookingStatusType.CHECKED_IN));
        return applicationConfig.getMaxPlots() - usedSpaceCount;
    }

    public boolean hasActiveBooking(final long customerId) {
        final CustomerEntity customer = customerRepository.findById(new CustomerId(customerId))
                .orElseThrow(() -> new IllegalStateException("customer id " + customerId + " not found"));
        final Set<BookingStatusType> bookingStatuses = Set.of(BookingStatusType.BOOKED, BookingStatusType.CHECKED_IN);
        final Long activeBookingCount = bookingRepository.countByCustomerIdAndStatusIn(customer.getId(), bookingStatuses);

        return activeBookingCount > 0L;
    }

    public BookingEntity create(final long customerId, final long vehicleId) {
        final CustomerEntity customer = customerRepository.findById(new CustomerId(customerId))
                .orElseThrow(() -> new IllegalStateException("customer id " + customerId + " not found"));
        final VehicleEntity vehicle = vehicleRepository.findById(new VehicleId(vehicleId))
                .orElseThrow(() -> new IllegalStateException("vehicle id " + vehicleId + " not found"));

        final Long nextId = bookingRepository.getNextId();
        final BookingEntity booking = BookingEntity.builder()
                .id(new BookingId(nextId))
                .customerId(customer.getId())
                .vehicleId(vehicle.getId())
                .baseFee(applicationConfig.getBaseFee())
                .status(BookingStatusType.BOOKED)
                .barcode(customerId + ":" + vehicleId + ":" + nextId).build();

        return bookingRepository.save(booking);
    }

    public BookingEntity getActiveBooking(final long customerId) {
        final CustomerEntity customer = customerRepository.findById(new CustomerId(customerId))
                .orElseThrow(() -> new IllegalStateException("customer id " + customerId + " not found"));
        final Set<BookingStatusType> bookingStatuses = Set.of(BookingStatusType.BOOKED, BookingStatusType.CHECKED_IN);

        return bookingRepository.findOneByCustomerIdAndStatusIn(customer.getId(), bookingStatuses).orElse(null);
    }

    public String generateCode39BarcodeImage(final String barcodeText) throws Exception {
        final Barcode barcode = BarcodeFactory.createCode39(barcodeText, false);
        barcode.setLabel(barcodeText);

        final BufferedImage img = BarcodeImageHandler.getImage(barcode);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        ImageIO.write(img, "png", stream);

        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }
}
