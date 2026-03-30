package com.bookmyshow.service;

import com.bookmyshow.enums.BookingStatus;
import com.bookmyshow.enums.PaymentStatus;
import com.bookmyshow.enums.SeatStatus;
import com.bookmyshow.exception.BookingNotFoundException;
import com.bookmyshow.exception.InvalidOperationException;
import com.bookmyshow.exception.PaymentFailedException;
import com.bookmyshow.exception.SeatsUnavailableException;
import com.bookmyshow.model.Booking;
import com.bookmyshow.model.Payment;
import com.bookmyshow.model.Seat;
import com.bookmyshow.model.Show;
import com.bookmyshow.payment.PaymentProcessor;
import com.bookmyshow.user.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BookingService {

    private static final int LOCK_DURATION_MINUTES = 10;

    private List<Booking> allBookings;
    private PaymentService paymentService;

    public BookingService() {
        this.allBookings = new ArrayList<>();
        this.paymentService = new PaymentService();
    }

    public synchronized Booking lockSeats(Show show, List<Seat> requestedSeats, Customer customer) {
        releaseExpiredLocks(show);

        for (Seat seat : requestedSeats) {
            SeatStatus status = show.getSeatStatus(seat.getId());
            if (status != SeatStatus.AVAILABLE) {
                throw new SeatsUnavailableException("Seat " + seat.getRow() + seat.getSeatNumber() + " is not available.");
            }
        }

        for (Seat seat : requestedSeats) {
            show.setSeatStatus(seat.getId(), SeatStatus.LOCKED);
            show.setLockExpiry(seat.getId(), LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
        }

        double totalAmount = 0;
        for (Seat seat : requestedSeats) {
            totalAmount += seat.getPrice();
        }

        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingId, customer, show, requestedSeats, totalAmount);
        allBookings.add(booking);

        System.out.println("Seats locked for booking " + bookingId + ". You have 10 minutes to pay.");
        return booking;
    }

    public Booking confirmBooking(Booking booking, PaymentProcessor processor) {
        Payment payment = paymentService.process(booking.getTotalAmount(), processor);

        if (payment.getStatus() == PaymentStatus.FAILED) {
            releaseSeats(booking.getShow(), booking.getSeats());
            booking.setStatus(BookingStatus.CANCELLED);
            throw new PaymentFailedException("Payment failed for booking " + booking.getId());
        }

        for (Seat seat : booking.getSeats()) {
            booking.getShow().setSeatStatus(seat.getId(), SeatStatus.BOOKED);
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPayment(payment);
        booking.getCustomer().addBooking(booking);

        System.out.println("Booking confirmed! Booking id: " + booking.getId());
        return booking;
    }

    public void cancelBooking(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidOperationException("Booking is already cancelled.");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            Payment payment = booking.getPayment();
            System.out.println("Processing refund of Rs." + payment.getAmount() + ".");
        }

        releaseSeats(booking.getShow(), booking.getSeats());
        booking.setStatus(BookingStatus.CANCELLED);

        System.out.println("Booking " + booking.getId() + " cancelled.");
    }

    public Booking findBookingById(String id) {
        for (Booking booking : allBookings) {
            if (booking.getId().equals(id)) {
                return booking;
            }
        }
        throw new BookingNotFoundException("Booking not found with id: " + id);
    }

    private void releaseSeats(Show show, List<Seat> seats) {
        for (Seat seat : seats) {
            show.setSeatStatus(seat.getId(), SeatStatus.AVAILABLE);
            show.setLockExpiry(seat.getId(), null);
        }
    }

    private void releaseExpiredLocks(Show show) {
        Map<String, SeatStatus> seatStatusMap = show.getSeatStatusMap();
        for (Map.Entry<String, SeatStatus> entry : seatStatusMap.entrySet()) {
            if (entry.getValue() == SeatStatus.LOCKED) {
                String seatId = entry.getKey();
                LocalDateTime expiry = show.getLockExpiry(seatId);
                if (expiry != null && LocalDateTime.now().isAfter(expiry)) {
                    seatStatusMap.put(seatId, SeatStatus.AVAILABLE);
                    System.out.println("Lock expired for seat " + seatId + ", releasing.");
                }
            }
        }
    }
}
