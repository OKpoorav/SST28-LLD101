package com.bookmyshow.model;

import com.bookmyshow.enums.SeatStatus;
import com.bookmyshow.enums.SeatType;

public class Seat {

    private String id;
    private String row;
    private int seatNumber;
    private SeatType seatType;
    private SeatStatus status;
    private double price;

    public Seat(String id, String row, int seatNumber, SeatType seatType, double price) {
        this.id = id;
        this.row = row;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }
}
