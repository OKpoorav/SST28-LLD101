package com.bookmyshow.user;

import com.bookmyshow.model.Booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {

    private List<Booking> bookingHistory;

    public Customer(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.bookingHistory = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
    }

    public List<Booking> getBookingHistory() {
        return Collections.unmodifiableList(bookingHistory);
    }
}
