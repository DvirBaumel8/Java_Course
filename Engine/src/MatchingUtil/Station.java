package MatchingUtil;

import Time.Time;

public class Station {
    private String name;
    private Time time;

    public String getName() {
        return name;
    }

    public int getDay() {
        return time.getDay();
    }

    public int getHour() {
        return time.getHours();
    }

    public int getMinutes() {
        return time.getMinutes();
    }
}
