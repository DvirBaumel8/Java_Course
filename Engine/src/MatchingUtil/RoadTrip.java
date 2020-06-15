package MatchingUtil;

import Time.Time;
import TripSuggestUtil.TripSuggest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadTrip {
    private Map<TripSuggest,String[]> participantSuggestTripsToRoadPart;
    private double totalCost;
    private double requiredFuel;
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

    public Map<TripSuggest, String[]> getParticipantSuggestTripsToRoadPart() {
        return participantSuggestTripsToRoadPart;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(Map.Entry<TripSuggest, String[]> entry : participantSuggestTripsToRoadPart.entrySet()) {
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

    public void addSuggestToRoadTrip(TripSuggest suggest, String[] stations) {
        this.participantSuggestTripsToRoadPart.put(suggest, stations);
    }


    public void calcTotalCost() {
    }

    public void calcRequiredFuel() {
    }

    public void calcStartArrivalTime() {
    }

    public void buildRoadTripStory() {
    }
}
