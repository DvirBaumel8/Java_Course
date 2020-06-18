package MatchingUtil;

import Time.Time;

public class Station {
    private String name;
    private Time arrivalTime;

    public Station(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return arrivalTime.getDay();
    }

    public int getHour() {
        return arrivalTime.getHours();
    }

    public int getMinutes() {
        return arrivalTime.getMinutes();
    }

    public Time getTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time time) {
        this.arrivalTime = time;
    }
}
