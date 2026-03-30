package com.bookmyshow.model;

import com.bookmyshow.enums.BookingStatus;
import com.bookmyshow.user.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class Booking {

    private String id;
    private Customer customer;
    private Show show;
    private List<Seat> seats;
    private BookingStatus status;
    private Payment payment;
    private double totalAmount;
    private LocalDateTime createdAt;

    public Booking(String id, Customer customer, Show show, List<Seat> seats, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.show = show;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
