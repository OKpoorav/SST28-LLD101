package com.bookmyshow.model;

public class Movie {

    private String id;
    private String title;
    private int durationMinutes;
    private String language;
    private String genre;

    public Movie(String id, String title, int durationMinutes, String language, String genre) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.language = language;
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getLanguage() {
        return language;
    }

    public String getGenre() {
        return genre;
    }
}
