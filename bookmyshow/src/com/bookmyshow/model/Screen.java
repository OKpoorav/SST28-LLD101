package com.bookmyshow.model;

import com.bookmyshow.enums.ScreenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Screen {

    private String id;
    private String name;
    private ScreenType screenType;
    private Theater theater;
    private List<Seat> seats;

    public Screen(String id, String name, ScreenType screenType, Theater theater) {
        this.id = id;
        this.name = name;
        this.screenType = screenType;
        this.theater = theater;
        this.seats = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public Theater getTheater() {
        return theater;
    }
}
