package com.bookmyshow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Theater {

    private String id;
    private String name;
    private City city;
    private List<Screen> screens;

    public Theater(String id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public List<Screen> getScreens() {
        return Collections.unmodifiableList(screens);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public City getCity() {
        return city;
    }
}
