package com.example.android.foodfinder.model;

import java.util.List;

/**
 * Created by ameya on 5/20/2017.
 */
public class RestaurantTimings {

    boolean open_now;
    List<String> weekday_text;

    public boolean isOpen_now() {
        return open_now;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }

    public List<String> getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(List<String> weekday_text) {
        this.weekday_text = weekday_text;
    }
}
