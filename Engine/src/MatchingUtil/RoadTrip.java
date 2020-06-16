package MatchingUtil;

import Manager.EngineManager;
import Time.Time;
import TripSuggestUtil.TripSuggest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadTrip {
    private Map<TripSuggest,Route> participantSuggestTripsToRoadPart;
    private int totalCost;
    private int requiredFuel;
    private Time startArrivalTime;
    private String RoadStory;
    private List<TripSuggest> ratedTripSuggested;

    public RoadTrip() {
        participantSuggestTripsToRoadPart = new HashMap<>();
        ratedTripSuggested = new ArrayList<>();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getRequiredFuel() {
        return requiredFuel;
    }

    public Time getStartArrivalTime() {
        return startArrivalTime;
    }

    public String getRoadStory() {
        return RoadStory;
    }

    public Map<TripSuggest, Route> getParticipantSuggestTripsToRoadPart() {
        return participantSuggestTripsToRoadPart;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(Map.Entry<TripSuggest, Route> entry : participantSuggestTripsToRoadPart.entrySet()) {
            //Print Road trip story
            //        str.append(String.format("Trip ID - %d\n", trip.getSuggestID()));
//        str.append(String.format("Trip owner name - %s\n", trip.getTripOwnerName()));
//        str.append(String.format("Trip price - %d\n", trip.getTripPrice()));
//        str.append(String.format("Trip estimate time to arrival - %s\n", convertDoubleTimeToStrTime(trip.getArrivalHourToSpecificStation(tripRequest.getDestinationStation()))));
//        str.append(String.format("Required fuel to your trip- %d\n", calcRequiredFuelToRequest(trip, tripRequest)));
        }

        return str.toString();
    }

    public List<TripSuggest> getRatedTripSuggested() {
        return ratedTripSuggested;
    }

    public void addSuggestToRoadTrip(TripSuggest suggest, Route route) {
        this.participantSuggestTripsToRoadPart.put(suggest, route);
    }

    public void calcTotalCost() {
        int cost = 0;
        for(Map.Entry<TripSuggest,Route> entry : participantSuggestTripsToRoadPart.entrySet()) {
            cost += calculateRoutePriceByPpk(entry.getKey().getPpk(), entry.getValue());
        }

        this.totalCost = cost;
    }

    public void calcRequiredFuel() {
        int fuel = 0;

        for(Map.Entry<TripSuggest,Route> entry : participantSuggestTripsToRoadPart.entrySet()) {
            fuel += entry.getKey().calcRequiredFuel(entry.getValue());
        }
        this.requiredFuel = fuel;
    }

    public void calcStartArrivalTime() {
    }

    public void buildRoadTripStory() {
    }

    private int calculateRoutePriceByPpk(int ppk, Route route) {
        int sum = 0;
        String[] paths = route.getPath().split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            int km = getLengthBetweenStations(paths[i], paths[i+1]);
            sum += km * ppk;
        }
        return sum;
    }

    private int getLengthBetweenStations(String pathFrom, String pathTo) {
        return EngineManager.getEngineManagerInstance().getLengthBetweenStations(pathFrom, pathTo);
    }
}
