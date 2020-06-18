package MatchingUtil;

import TripSuggestUtil.TripSuggest;

import java.util.LinkedList;

public class SubTrip {
    private LinkedList<Station> stations;
    private int cost;
    private int requiredFuel;
    private TripSuggest trip;
    private int tripID;

    public SubTrip(SubTrip subTrip, int day) {
        this.stations = subTrip.getRoute();
        this.cost = subTrip.getCost();
        this.requiredFuel = subTrip.getRequiredFuel();
        this.trip = subTrip.getTrip();
        this.tripID = subTrip.getTripID();
    }

    public SubTrip(SubTrip subTrip) {
        this.stations = subTrip.getRoute();
        this.cost = subTrip.getCost();
        this.requiredFuel = subTrip.getRequiredFuel();
        this.trip = subTrip.getTrip();
    }

    public Station getLastStation() {
        return stations.getLast();
    }

    public Station getFirstStation() {
        return stations.getFirst();
    }

    public int getCost() {
        return cost;
    }

    public int getRequiredFuel() {
        return requiredFuel;
    }

    public TripSuggest getTrip() {
        return trip;
    }

    public LinkedList<Station> getRoute() {
        return stations;
    }

    public void setEndStationInRoute(Station last, int closestDayFromAbove) {
    }

    public void setStartStationInRoute(Station first, int closestDayFromBelow) {
    }

    public int getTripID() {
        return tripID;
    }
}
