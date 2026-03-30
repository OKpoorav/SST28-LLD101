package com.bookmyshow.model;

import com.bookmyshow.enums.SeatStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Show {

    private String id;
    private Movie movie;
    private Screen screen;
    private LocalDateTime startTime;
    private Map<String, SeatStatus> seatStatusMap;
    private Map<String, LocalDateTime> seatLockExpiry;

    public Show(String id, Movie movie, Screen screen, LocalDateTime startTime) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.seatStatusMap = new HashMap<>();
        this.seatLockExpiry = new HashMap<>();

        for (Seat seat : screen.getSeats()) {
            seatStatusMap.put(seat.getId(), SeatStatus.AVAILABLE);
        }
    }

    public SeatStatus getSeatStatus(String seatId) {
        return seatStatusMap.get(seatId);
    }

    public void setSeatStatus(String seatId, SeatStatus status) {
        seatStatusMap.put(seatId, status);
    }

    public void setLockExpiry(String seatId, LocalDateTime expiry) {
        seatLockExpiry.put(seatId, expiry);
    }

    public LocalDateTime getLockExpiry(String seatId) {
        return seatLockExpiry.get(seatId);
    }

    public String getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Map<String, SeatStatus> getSeatStatusMap() {
        return seatStatusMap;
    }
}
