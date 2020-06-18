package MatchingUtil;

import Time.Time;
import TripSuggestUtil.TripSuggest;

import java.util.LinkedList;

public class SubTrip {
    private LinkedList<Station> stations;
    private int cost;
    private int requiredFuel;
    private TripSuggest trip;
    private int tripID;
    private int tripDay;
    private String subTripStory;
    private Time startTime;
    private Time arrivalTime;

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

    public SubTrip(TripSuggest tripsuggest, Station station, Station station1, int closestDayFromAbove) {
        this.trip = tripsuggest;
        stations = new LinkedList<>();
        stations.add(station);
        stations.add(station1);
        this.tripDay = closestDayFromAbove;
    }

    public void buildSubTripStory() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("Go up to %s's car in station %s\n", trip.getTripOwnerName(), stations.getFirst().getName()));
        str.append(String.format("Go down from %s's car in station %s\n",trip.getTripOwnerName(), stations.getLast().getName()));
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

    public String getSubTripStory() {
        return subTripStory;
    }

    public void calcCost() {
        this.cost = CalculatorUtil.calcCost(trip.getPpk(), stations);
    }

    public void calcRequiredFuel() {
        this.requiredFuel = CalculatorUtil.calcRequiredFuel(stations);
    }

    public void calcStartArrivalTime() {
        this.startTime = stations.getFirst().getTime();
        this.arrivalTime = stations.getLast().getTime();
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }
}
