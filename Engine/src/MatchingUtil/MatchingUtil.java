package MatchingUtil;

import Manager.EngineManager;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingUtil {
    private static final String SUCCESS_MATCHING = "Your trip request was match to trip suggested successfully\n";
    private Map<TripSuggest, List<TripRequest>> matches;

    public MatchingUtil() {
        matches = new HashMap<>();
    }

    public TripSuggest[] findPotentialMatchToRequestTrip(String input) {
        String[] inputs = input.split(",");
        int requestID = Integer.parseInt(inputs[0]);
        int suggestedAmountTrips = Integer.parseInt(inputs[1]);
        TripRequest requestTrip = EngineManager.getEngineManagerInstance().getTripRequestByID(requestID);
        Map<TripSuggest, Integer> suggestedTrips = EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap();

        return findPotentialMatches(requestTrip, suggestedTrips, suggestedAmountTrips);
    }

    private TripSuggest[] findPotentialMatches(TripRequest requestTrip, Map<TripSuggest, Integer> suggestedTrips, int suggestedAmountTrips) {
       TripSuggest[] potentialSuggestedTrips = new TripSuggest[suggestedAmountTrips];
        int counter = 0;

        for(Map.Entry<TripSuggest, Integer> trip : suggestedTrips.entrySet()) {
            if(checkIFSuggestedTripIncludeRequestStations(requestTrip.getSourceStation(), requestTrip.getDestinationStation(), trip.getKey())) {
                if((!requestTrip.isRequestByStartTime() && requestTrip.getRequestRequiredTime() == trip.getKey().getArrivalHourToSpecificStation(requestTrip.getDestinationStation()) ) || (requestTrip.isRequestByStartTime() && checkRequestTimeToSuggestTrip(requestTrip, trip.getKey()))) {
                    if(trip.getKey().getRemainingCapacity() > 0) {
                        if(counter < suggestedAmountTrips) {
                            potentialSuggestedTrips[counter] = trip.getKey();
                            counter++;
                            if(counter == suggestedAmountTrips) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(counter == 0) {
            return null;
        }
        return potentialSuggestedTrips;
    }

    private boolean checkRequestTimeToSuggestTrip(TripRequest tripRequest, TripSuggest tripSuggest) {
        double hour = tripSuggest.getArrivalHourToSpecificStation(tripRequest.getSourceStation());
        if(hour == tripRequest.getRequestRequiredTime()) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkIFSuggestedTripIncludeRequestStations(String sourceStation, String destinationStation, TripSuggest suggestedTrip) {
        String suggestedTripRoute = suggestedTrip.getTripRoute();
        String[] stations = suggestedTripRoute.split(",");
        int indexOfSourceDestination = -1;
        int indexOfDestinationStation = -1;

        for(int i = 0; i < stations.length; i++) {
            if(stations[i].equals(sourceStation)) {
                indexOfSourceDestination = i;
            }
            if(stations[i].equals(destinationStation)) {
                indexOfDestinationStation = i;
            }
        }
        if(indexOfSourceDestination < indexOfDestinationStation && indexOfDestinationStation != -1 && indexOfSourceDestination != -1) {
            return true;
        }
        return false;
    }

    public String matchRequestToSuggest(TripSuggest tripSuggest, TripRequest tripRequest) {
        if(checkIFSuggestedTripHasPassengers(tripSuggest)) {
            matches.get(tripSuggest).add(tripRequest);
        }
        else {
            List<TripRequest> requestsList = new ArrayList<>();
            requestsList.add(tripRequest);
            matches.put(tripSuggest, requestsList);
        }
        tripRequest.setMatched(true);
        tripRequest.setMatchTrip(tripSuggest);
        tripSuggest.addNewPassengerToTrip(tripRequest);
        return SUCCESS_MATCHING;
    }

    public boolean checkIFSuggestedTripHasPassengers(TripSuggest tripSuggest) {
        for(Map.Entry<TripSuggest, List<TripRequest>> trip : matches.entrySet()) {
            if(trip.getKey().getSuggestID() == tripSuggest.getSuggestID()) {
                return true;
            }
        }
        return false;
    }

    public Map<TripSuggest, List<TripRequest>> getMatches() {
        return matches;
    }

}
