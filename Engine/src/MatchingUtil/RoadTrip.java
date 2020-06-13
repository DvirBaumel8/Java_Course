package MatchingUtil;

import TripSuggestUtil.TripSuggest;

import java.util.HashMap;
import java.util.Map;

public class RoadTrip {
    private Map<TripSuggest,String> participantSuggestTripsToRoadPart;
    private String RoadStory;
    private int index;

    public RoadTrip() {
        participantSuggestTripsToRoadPart = new HashMap<>();
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(Map.Entry<TripSuggest, String> entry : participantSuggestTripsToRoadPart.entrySet()) {
            //Print Road trip story
            //        str.append(String.format("Trip ID - %d\n", trip.getSuggestID()));
//        str.append(String.format("Trip owner name - %s\n", trip.getTripOwnerName()));
//        str.append(String.format("Trip price - %d\n", trip.getTripPrice()));
//        str.append(String.format("Trip estimate time to arrival - %s\n", convertDoubleTimeToStrTime(trip.getArrivalHourToSpecificStation(tripRequest.getDestinationStation()))));
//        str.append(String.format("Required fuel to your trip- %d\n", calcRequiredFuelToRequest(trip, tripRequest)));
        }

        return str.toString();
    }
}
