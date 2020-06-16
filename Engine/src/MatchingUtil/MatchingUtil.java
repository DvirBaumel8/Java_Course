package MatchingUtil;

import Manager.EngineManager;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingUtil {
    private List<TripSuggest> suggestedTrips;
    private TripRequest tripRequest;

    public MatchingUtil() {
        suggestedTrips = new ArrayList<>();
    }

    public List<RoadTrip> findRoadTripsMatchToRequestTrip(String input) {
        String[] inputs = input.split(",");
        int requestID = Integer.parseInt(inputs[0]);
        int roadTripsLimit = Integer.parseInt(inputs[1]);
        tripRequest = EngineManager.getEngineManagerInstance().getTripRequestByID(requestID);
        Map<TripSuggest, Integer> suggestedTripsToId = EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap();

        for(Map.Entry<TripSuggest, Integer> trip : suggestedTripsToId.entrySet()) {
            suggestedTrips.add(trip.getKey());
        }

        return findPotentialMatches(roadTripsLimit);
    }

    private List<RoadTrip> findPotentialMatches(int suggestedAmountTrips) {
        List<RoadTrip> potentialSuggestedTrips = new ArrayList<>(suggestedAmountTrips);

        if(tripRequest.isRequestByStartTime()) {
            findPotentialMatchesToStartTime(potentialSuggestedTrips, suggestedAmountTrips);
        }
        else {
            findPotentialMatchToArrivalTime(potentialSuggestedTrips, suggestedAmountTrips);
        }
        calcRoadTripsValues(potentialSuggestedTrips);

//        for(Map.Entry<TripSuggest, Integer> trip : suggestedTrips.entrySet()) {
//            if(checkIFSuggestedTripIncludeRequestStations(requestTrip.getSourceStation(), requestTrip.getDestinationStation(), trip.getKey())) {
//                if((!requestTrip.isRequestByStartTime() && requestTrip.getRequestRequiredTime() == trip.getKey().getArrivalHourToSpecificStation(requestTrip.getDestinationStation()) ) || (requestTrip.isRequestByStartTime() && checkRequestTimeToSuggestTrip(requestTrip, trip.getKey()))) {
//                    if(trip.getKey().getRemainingCapacity() > 0) {
//                        if(counter < suggestedAmountTrips) {
//                            potentialSuggestedTrips.add(counter, trip.getKey());
//                            counter++;
//                            if(counter == suggestedAmountTrips) {
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        if(counter == 0) {
//            return null;
//        }
        return potentialSuggestedTrips;
    }

    private void calcRoadTripsValues(List<RoadTrip> potentialSuggestedTrips) {
        for(RoadTrip roadTrip : potentialSuggestedTrips) {
            calcRoadTripValues(roadTrip);
        }
    }

    private void calcRoadTripValues(RoadTrip roadTrip) {
        roadTrip.calcTotalCost();
        roadTrip.calcRequiredFuel();
        roadTrip.calcStartArrivalTime();
        roadTrip.buildRoadTripStory();
    }

    private void findPotentialMatchToArrivalTime(List<RoadTrip> potentialSuggestedTrips, int suggestedAmountTrips) {
//
    }

    private void findPotentialMatchesToStartTime(List<RoadTrip> potentialSuggestedTrips, int suggestedAmountTrips) {
        Route[] routes = new Route[suggestedAmountTrips];
        Route route1 = new Route();
        route1.setPath("A,B");
        Route route2 = new Route();
        route2.setPath("B,C");
        Route route3 = new Route();
        route3.setPath("C,D");
        routes[0] = route1;
        routes[1] = route2;
        routes[2] = route3;
        int index=0;
        RoadTrip currRoadTrip;
        for(TripSuggest suggest : suggestedTrips) {
            currRoadTrip = new RoadTrip();
            currRoadTrip.setTripRequest(tripRequest);
            currRoadTrip.addSuggestToRoadTrip(suggest, routes[index]);
            potentialSuggestedTrips.add(currRoadTrip);
            index++;
        }
//        int counter = 0;
//        for (TripSuggest trip : suggestedTrips) {
//            if (checkIfTripIncludeSourceStation(trip)) { //trip suggest get to request start station
//                if (checkIfTripArrivalHourToRequestSourceStationIsEquals(trip, tripRequest)) {
//                    if (checkIfTripCanBeMatch(trip, tripRequest)) {
//                        counter++;
//                        if (counter == suggestedAmountTrips) {
//                            break;
//                            for (Map.Entry<TripSuggest, Integer> trip : suggestedTrips.entrySet()) {
//                                if (checkIFSuggestedTripIncludeRequestStations(requestTrip.getSourceStation(), requestTrip.getDestinationStation(), trip.getKey())) {
//                                    if ((!requestTrip.getIsStartTime() && requestTrip.getRequestRequiredTime() == trip.getKey().getArrivalHourToSpecificStation(requestTrip.getDestinationStation())) || (requestTrip.getIsStartTime() && checkRequestTimeToSuggestTrip(requestTrip, trip.getKey()))) {
//                                        if (trip.getKey().getRemainingCapacity() > 0) {
//                                            if (counter < suggestedAmountTrips) {
//                                                potentialSuggestedTrips[counter] = trip.getKey();
//                                                counter++;
//                                                if (counter == suggestedAmountTrips) {
//                                                    break;
//                                                }
//                                            }
//                                            //Create new RoadTrip with single trip suggest
//                                        } else {
//                                            String[] shortRoute = shortcutRootToRequestSourceDest(trip.getTripRoute(), tripRequest.getSourceStation());
//                                            handleShortRouteAction(potentialSuggestedTrips, tripRequest, trip, shortRoute, suggestedAmountTrips);
//                                        }
////                                    }
////                                }
////                            }
//
//                        }
//                    }
//                }
//            }
//        }
    }

    private boolean checkIfTripCanBeMatch(TripSuggest tripSuggest, TripRequest requestTrip) {
        String[] stations = tripSuggest.getTripRoute().split(",");
        int indexOfSource = -1;
        int indexOfDest = -1;

        for(int i =0; i < stations.length; i++) {
            if(stations[i].equals(requestTrip.getSourceStation())) {
                indexOfSource = i;
            }
            if(stations[i].equals(requestTrip.getDestinationStation())) {
                indexOfDest = i;
            }
        }

        if(indexOfDest != -1 && indexOfSource != -1) {
            return indexOfSource < indexOfDest;
        }

        return false;
    }

    private void handleShortRouteAction(List<RoadTrip> potentialSuggestedTrips, TripRequest requestTrip, TripSuggest suggestTrip, String[] shortRoute, int suggestedAmountTrips) {
        for(int i = 1; i < shortRoute.length; i++) {

        }
        if(shortRoute.length == 2) {
            if(shortRoute[1].equals(requestTrip.getDestinationStation())) {
                //found
            }
            else {

            }
        }
        else {

        }
    }

    public boolean handleShortRouteAction(Map<TripSuggest, Integer> suggestedTrip, TripRequest tripRequest, String[] shortRoute) {
        if(isCurrentShortRouteSatisfied(shortRoute, tripRequest)) {
            return true;
        }
        else {
            for(int i = 0; i < shortRoute.length; i++) {
                TripSuggest[] suggestedTrips = findSuggestedTripsWithCurrStation(shortRoute[i]);
                handleShortRouteAction(suggestedTrip, tripRequest, getShortRouteWithoutFirstElement(shortRoute));
            }

        }
        return false;
    }

    private TripSuggest[] findSuggestedTripsWithCurrStation(String station) {
        return null;
    }

    private String[] getShortRouteWithoutFirstElement(String[] shortRoute) {
        int size = shortRoute.length - 1;
        String[] ret = new String[size];

        for(int i = 1; i < shortRoute.length; i++) {
            ret[i-1] = shortRoute[i];
        }
        return ret;
    }

    private boolean isCurrentShortRouteSatisfied(String[] shortRoute, TripRequest tripRequest) {
        for(int i = 0; i < shortRoute.length; i++) {
            if(shortRoute[i].equals(tripRequest.getDestinationStation())) {
                return true;
            }
        }
        return false;
    }

    public static String[] shortcutRootToRequestSourceDest(String tripRoute, String sourceStation) {
        String[] stations = tripRoute.split(",");
        String[] newRoute;
        int index = 0;
        boolean stationFound = false;

        for(int i = 0; i < stations.length; i++) {
            if(stations[i].equals(sourceStation)) {
                index = i;
                break;
            }
        }
        newRoute = new String[stations.length - index];

        for(int i = index, j = 0; i < stations.length; i++, j++) {
            newRoute[j] = stations[i];
        }

        return newRoute;
    }

    public static void main(String[] args) {
        String[] answer = shortcutRootToRequestSourceDest("A,B,C,D,E,F", "C");
        System.out.println("");
    }

    private boolean checkIfSuggestedTripIncludeRequestDestStation(TripSuggest trip, String destStation, String sourceStation) {
        String[] route = trip.getTripRoute().split(",");
        for(int i = 0; i < route.length - 1; i++) {
            if(route[i].equals(destStation)) {
                int index = getSourceStationIndexInRoute(trip, sourceStation);
                return i > index;
            }
        }
        return false;
    }

    private int getSourceStationIndexInRoute(TripSuggest trip, String sourceStation) {
        String[] route = trip.getTripRoute().split(",");
        for(int i = 0; i < route.length - 1; i++) {
            if(route[i].equals(sourceStation)) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkIfTripArrivalHourToRequestSourceStationIsEquals(TripSuggest trip, TripRequest tripRequest) {
        //double arrivalTimeToSourceStation = trip.getArrivalHourToSpecificStation(tripRequest.getSourceStation());
        //return arrivalTimeToSourceStation == tripRequest.getRequestRequiredTime();
        return true;
    }

    private boolean checkIfTripIncludeSourceStation(TripSuggest trip) {
        String[] route = trip.getTripRoute().split(",");
        for(int i = 0; i < route.length - 1; i++) {
            if(route[i].equals(tripRequest.getSourceStation())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRequestTimeToSuggestTrip(TripRequest tripRequest, TripSuggest tripSuggest) {
        //double hour = tripSuggest.getArrivalHourToSpecificStation(tripRequest.getSourceStation());
//       // if(hour == tripRequest.getRequestRequiredTime()) {
//           // return true;
//        //}
//        else {
        return false;
//        }
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

    private boolean checkIfSuggestedTripStartAtRequestDay(TripSuggest suggest) {
//        int requestRequiredDay = tripRequest.getTripDay();
//        int suggestStartingDay = suggest.getTripStartDay();
//        int temp;
//
//        if(requestRequiredDay < suggestStartingDay) {
//            return false;
//        }
//        else if(requestRequiredDay == suggestStartingDay) {
//            return true;
//        }
//        else {
//            switch (suggest.getRecurrencesType()) {
//                case ONE_TIME_ONLY: {
//                    if(requestRequiredDay != suggestStartingDay) {
//                        return false;
//                    }
//                    else {
//                        return true;
//                    }
//                }
//                case DAILY: {
//                    temp = suggestStartingDay;
//                    while(temp < requestRequiredDay) {
//                        if(temp == requestRequiredDay) {
//                            return true;
//                        }
//                        temp++;
//                    }
//                    return false;
//                }
//                case BI_DAILY: {
//                    temp = suggestStartingDay;
//                    while(temp < requestRequiredDay) {
//                        if(temp == requestRequiredDay) {
//                            return true;
//                        }
//                        temp+=2;
//                    }
//                    return false;
//
//                }
//                case WEEKLY: {
//                    temp = suggestStartingDay;
//                    while(temp < requestRequiredDay) {
//                        if(temp == requestRequiredDay) {
//                            return true;
//                        }
//                        temp+=7;
//                    }
//                    return false;
//
//                }
//                case MONTHLY: {
//                    temp = suggestStartingDay;
//                    while(temp < requestRequiredDay) {
//                        if(temp == requestRequiredDay) {
//                            return true;
//                        }
//                        temp+=30;
//                    }
//                    return false;
//
//                }
//
//            }
//        }
//
                            return false;
                        }

                    }
