package MatchingUtil;

import Manager.EngineManager;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import XML.XMLLoading.jaxb.schema.generated.Route;
import XML.XMLLoading.jaxb.schema.generated.TransPool;
import org.omg.CORBA.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MatchingUtil {
//
    public MatchingUtil(TransPool system) {
        data = system;
    }

    private TransPool data;

    public LinkedList<LinkedList<SubTrip>> makeAMatch(TripRequest request, int numberOfTripsToOffer) {  //By departure or arrival
        if (request.isRequestByStartTime())
            return collectMatchingTripsByDepartureTime(request, numberOfTripsToOffer);
        else
            return collectMatchingTripsByArrivalTime(request, numberOfTripsToOffer);
    }

    private LinkedList<LinkedList<SubTrip>> collectMatchingTripsByDepartureTime(TripRequest request, int numberOfTripsToOffer) {

        int index;
        LinkedList<SubTrip> combinedTrip = new LinkedList<>();
        LinkedList<LinkedList<SubTrip>> res = new LinkedList<>();

        for (Map.Entry<Integer, TripSuggest> entry : EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap().entrySet()) {
            for (index = 0; index < entry.getValue().getRide().length - 1; index++) {
                if (entry.getValue().getRide()[index].getName().equals(request.getSourceStation()) &&
                        isTripTimeEqual(request.getStartTime().getDay(), request.getStartTime().getHours(), request.getStartTime().getMinutes(), entry.getValue().getRide()[index], entry.getValue())) {
                    //combinedTrip.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index], entry.getValue().getRide()[index + 1], findClosestDayFromAbove(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], request.getStartTime().getDay(), request.getStartTime().getHours(), request.getStartTime().getMinutes())));
                    buildMatchingTripsByDeparture(combinedTrip, request, res, numberOfTripsToOffer);
                    break;
                }
            }
        }
        return res;
    }

    private void buildMatchingTripsByDeparture(LinkedList<SubTrip> ride, TripRequest request, LinkedList<LinkedList<SubTrip>> res, int numberOfTripsToOffer) {

        if (res.size() == numberOfTripsToOffer)
            return;

        Station current = ride.getLast().getLastStation();
        if (request.getDestinationStation().equals(current)) {
            res.add(copyLinkedList(ride));
        } else {
            LinkedList<SubTrip> matchingRides = findTripsForNextStations(ride);
            for (SubTrip subTrip : matchingRides) {
                LinkedList<SubTrip> newRide = copyLinkedList(ride);
                if (subTrip.getTrip().getSuggestID() == ride.getLast().getTrip().getSuggestID()) {
                    newRide.getLast().setEndStationInRoute(subTrip.getRoute().getLast(), findClosestDayFromAbove(subTrip.getTrip(), subTrip.getTrip().getRecurrencesType().getValue(), subTrip.getFirstStation(), newRide.getLast().getLastStation().getDay(), newRide.getLast().getLastStation().getHour(), newRide.getLast().getLastStation().getMinutes()));
                    buildMatchingTripsByDeparture(copyLinkedList(newRide), request, res, numberOfTripsToOffer);
                    newRide.getLast().getRoute().removeLast();
                } else {
                    newRide.add(new SubTrip(subTrip, findClosestDayFromAbove(subTrip.getTrip(), subTrip.getTrip().getRecurrencesType().getValue(), subTrip.getFirstStation(), newRide.getLast().getLastStation().getDay(), newRide.getLast().getLastStation().getHour(), newRide.getLast().getLastStation().getMinutes())));
                    buildMatchingTripsByDeparture(copyLinkedList(newRide), request, res, numberOfTripsToOffer);
                    newRide.removeLast();
                }
            }
        }
    }

    private LinkedList<SubTrip> copyLinkedList(LinkedList<SubTrip> ride) {
        LinkedList newList = new LinkedList<SubTrip>();
        for (SubTrip subTrip : ride) {
            newList.add(new SubTrip(subTrip));
        }
        return newList;
    }


    public LinkedList<SubTrip> findTripsForNextStations(LinkedList<SubTrip> ride) {

        LinkedList<SubTrip> matchingRides = new LinkedList<>();
        Station currentStation = ride.getLast().getLastStation();
        int index;
        for (Map.Entry<Integer, TripSuggest> entry : EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap().entrySet()) {
            for (index = 0; index < entry.getValue().getRide().length - 1; index++) {
                if (entry.getValue().getRide()[index].getName().equals(currentStation.getName()) &&
                        isTripTimeBigger(currentStation, entry.getValue().getRide()[index], entry.getValue()) &&
                        isTheStationExist(ride, entry.getValue().getRide()[index + 1]));
                   // matchingRides.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index], entry.getValue().getRide()[index + 1], findClosestDayFromAbove(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], ride.getLast().getLastStation().getDay(), ride.getLast().getLastStation().getHour(), ride.getLast().getLastStation().getMinutes())));
            }
        }
        return matchingRides;
    }

    private boolean isTheStationExist(LinkedList<SubTrip> ride, Station station) {
        for (SubTrip mr : ride) {
            for (Station st : mr.getRoute()) {
                if (st.getName().equals(station.getName()))
                    return false;
            }
        }
        return true;
    }

    private boolean isTripTimeEqual(int day, int hour, int minutes, Station tripStation, TripSuggest trip) {
        int recurrences = trip.getRecurrencesType().getValue();
        if (tripStation.getDay() <= day) {
            if ((tripStation.getDay() * 24 * 60) + (tripStation.getHour() * 60) + (tripStation.getMinutes()) < ((day * 60 * 24) + (hour * 60) + (minutes)) && trip.getRecurrencesType().equals("ONE_TIME_ONLY"))
                return false;
            if ((tripStation.getDay() % recurrences) == day % recurrences && tripStation.getHour() == hour && tripStation.getMinutes() == minutes && (!trip.getCapacityPerTime().containsKey("" + day + hour + minutes) || (trip.getCapacityPerTime().containsKey("" + day + hour + minutes) && trip.getCapacityPerTime().get("" + day + hour + minutes) > 0)))
                return true;
        }
        return false;
    }

    private boolean isTripTimeBigger(Station requestStation, Station tripStation, TripSuggest trip) {
        if (requestStation.getDay() > tripStation.getDay() && trip.getRecurrencesType().equals("ONE_TIME_ONLY"))
            return false;
        else if (trip.getRecurrencesType().equals("ONE_TIME_ONLY") && (trip.getCapacityPerTime().containsKey("" + trip.getStartingTime().getDay() + trip.getStartingTime().getHours() + trip.getStartingTime().getMinutes()) && trip.getCapacityPerTime().get("" + trip.getStartingTime().getDay() + trip.getStartingTime().getHours() + trip.getStartingTime().getMinutes()) == 0))
            return false;
        else
            return true;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~arrival~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    private LinkedList<LinkedList<SubTrip>> collectMatchingTripsByArrivalTime(TripRequest request, int numberOfTripsToOffer) {

        LinkedList<SubTrip> combinedTrip = new LinkedList<>();
        LinkedList<LinkedList<SubTrip>> res = new LinkedList<>(); // save all the optional result of combinedTrips

        for (Map.Entry<Integer, TripSuggest> entry : EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap().entrySet()) {
            for (int index = entry.getValue().getRide().length - 1; index > 0; index--) {
                if (isTripTimeEqual(request.getArrivalTime().getDay(), request.getArrivalTime().getHours(), request.getArrivalTime().getMinutes(), entry.getValue().getRide()[index], entry.getValue())) {
                   // combinedTrip.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index - 1], entry.getValue().getRide()[index], findClosestDayFromBelow(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], request.getArrivalTime().getDay(), request.getArrivalTime().getHours(), request.getArrivalTime().getMinutes())));
                    buildMatchingTripsByArrival(combinedTrip, request, res, numberOfTripsToOffer); //send them to a rec function to find all optional trips
                    break;
                }
            }
        }
        return res;
    }

    private void buildMatchingTripsByArrival(LinkedList<SubTrip> ride, TripRequest request, LinkedList<LinkedList<SubTrip>> res, int numberOfTripsToOffer) {

        if (res.size() == numberOfTripsToOffer)
            return;

        Station current = ride.getFirst().getRoute().getFirst();
        if (request.getSourceStation().equals(current.getName())) { // if we in the destination
            res.add(ride);
        } else {
            LinkedList<SubTrip> matchingRides = findTripsForPrevStations(ride); //gating list of optional Plaid drive
            for (SubTrip subTrip : matchingRides) {
                LinkedList<SubTrip> newRide = copyLinkedList(ride);
                if (subTrip.getTrip().getSuggestID() == ride.getFirst().getTrip().getSuggestID()) {// if its the same drive just chang the end station
                    newRide.getFirst().setStartStationInRoute(subTrip.getRoute().getFirst(), findClosestDayFromBelow(subTrip.getTrip(), subTrip.getTrip().getRecurrencesType().getValue(), subTrip.getRoute().getLast(), newRide.getLast().getRoute().getFirst().getDay(), newRide.getLast().getRoute().getFirst().getHour(), newRide.getLast().getRoute().getFirst().getMinutes()));
                    buildMatchingTripsByArrival(newRide, request, res, numberOfTripsToOffer);
                } else {
                    newRide.addFirst(new SubTrip(subTrip, findClosestDayFromBelow(subTrip.getTrip(), subTrip.getTrip().getRecurrencesType().getValue(), subTrip.getRoute().getLast(), newRide.getFirst().getRoute().getFirst().getDay(), newRide.getFirst().getRoute().getFirst().getHour(), newRide.getFirst().getRoute().getFirst().getMinutes())));
                    buildMatchingTripsByArrival(copyLinkedList(newRide), request, res, numberOfTripsToOffer);
                    newRide.removeFirst();
                }
            }
        }
    }

    private LinkedList<SubTrip> findTripsForPrevStations(LinkedList<SubTrip> ride) {

        LinkedList<SubTrip> matchingRides = new LinkedList<>();
        Station current = ride.getFirst().getRoute().getFirst();
        for (Map.Entry<Integer, TripSuggest> entry : EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap().entrySet()) {
            for (int index = entry.getValue().getRide().length - 1; index > 0; index--) {  // 0<i because if it is the lest station it cant continue withe driver
                if (entry.getValue().getRide()[index].getName().equals(current.getName()) &&
                        isTripTimeSmaller(entry.getValue(), current, entry.getValue().getRide()[index]) &&
                        isTheStationExist(ride, entry.getValue().getRide()[index - 1]));
                   // matchingRides.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index - 1], entry.getValue().getRide()[index], findClosestDayFromBelow(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], ride.getFirst().getRoute().getFirst().getDay(), ride.getFirst().getRoute().getFirst().getHour(), ride.getFirst().getRoute().getFirst().getMinutes())));
            }

        }
        return matchingRides;
    }

    private boolean isTripTimeSmaller(TripSuggest trip, Station current, Station station) {
        int newDay = station.getDay();
        while (((current.getDay() * 24 * 60) + (current.getHour() * 60) + (current.getMinutes())) >= ((newDay * 24 * 60) + (station.getHour() * 60) + (station.getMinutes()))) {
            if ((!trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes())) ||
                    (trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes()) && trip.getCapacityPerTime().get("" + newDay + station.getHour() + station.getMinutes()) > 0))
                return true;
            else if (trip.getRecurrencesType().equals("ONE_TIME_ONLY"))
                return false;
            else
                newDay += trip.getRecurrencesType().getValue();
        }
        return false;
    }

    private int findClosestDayFromAbove(TripSuggest trip, int recurrences, Station station, int day, int hour, int minutes) {
        int newDay = station.getDay();
        int time = (newDay * 24 * 60) + (station.getHour() * 60) + (station.getMinutes());
        while (time < ((day * 24 * 60) + (hour * 60) + (minutes))) {
            newDay += recurrences;
            time = newDay * 24 * 60 + station.getHour() * 60 + station.getMinutes();
        }
        while (trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes()) && trip.getCapacityPerTime().get("" + newDay + station.getHour() + station.getMinutes()) == 0)
            newDay += recurrences;

        return newDay - station.getDay();
    }

    private int findClosestDayFromBelow(TripSuggest trip, int recurrences, Station station, int day, int hour, int minutes) {
        int newDay = station.getDay();
        int time = (newDay * 24 * 60) + (station.getHour() * 60) + (station.getMinutes());

        if (trip.getRecurrencesType().equals("ONE_TIME_ONLY"))
            return 0;

        while (time <= ((day * 24 * 60) + (hour * 60) + (minutes))) {
            newDay += recurrences;
            time = newDay * 24 * 60 + station.getHour() * 60 + station.getMinutes();
        }
        while (trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes()) && trip.getCapacityPerTime().get("" + newDay + station.getHour() + station.getMinutes()) == 0)
            newDay -= recurrences;

        if (time > station.getDay() * 24 * 60 + station.getHour() * 60 + station.getMinutes())
            return (newDay - recurrences) - station.getDay();
        else
            return 0;
    }



    //--------------------Before----------------
//    private List<TripSuggest> suggestedTrips;
//    private TripRequest tripRequest;
//
//    public MatchingUtil() {
//        suggestedTrips = new ArrayList<>();
//    }
//
//    public List<RoadTrip> findRoadTripsMatchToRequestTrip(String input) {
//        String[] inputs = input.split(",");
//        int requestID = Integer.parseInt(inputs[0]);
//        int roadTripsLimit = Integer.parseInt(inputs[1]);
//        tripRequest = EngineManager.getEngineManagerInstance().getTripRequestByID(requestID);
//        Map<TripSuggest, Integer> suggestedTripsToId = EngineManager.getEngineManagerInstance().getAllSuggestedTripsMap();
//
//        for(Map.Entry<TripSuggest, Integer> trip : suggestedTripsToId.entrySet()) {
//            suggestedTrips.add(trip.getKey());
//        }
//
//        return findPotentialMatches(roadTripsLimit);
//    }
//
//    private List<RoadTrip> findPotentialMatches(int suggestedAmountTrips) {
//        List<RoadTrip> potentialSuggestedTrips = new ArrayList<>(suggestedAmountTrips);
//
//        if(tripRequest.isRequestByStartTime()) {
//            findPotentialMatchesToStartTime(potentialSuggestedTrips, suggestedAmountTrips);
//        }
//        else {
//            findPotentialMatchToArrivalTime(potentialSuggestedTrips, suggestedAmountTrips);
//        }
//        calcRoadTripsValues(potentialSuggestedTrips);
//
////        for(Map.Entry<TripSuggest, Integer> trip : suggestedTrips.entrySet()) {
////            if(checkIFSuggestedTripIncludeRequestStations(requestTrip.getSourceStation(), requestTrip.getDestinationStation(), trip.getKey())) {
////                if((!requestTrip.isRequestByStartTime() && requestTrip.getRequestRequiredTime() == trip.getKey().getArrivalHourToSpecificStation(requestTrip.getDestinationStation()) ) || (requestTrip.isRequestByStartTime() && checkRequestTimeToSuggestTrip(requestTrip, trip.getKey()))) {
////                    if(trip.getKey().getRemainingCapacity() > 0) {
////                        if(counter < suggestedAmountTrips) {
////                            potentialSuggestedTrips.add(counter, trip.getKey());
////                            counter++;
////                            if(counter == suggestedAmountTrips) {
////                                break;
////                            }
////                        }
////                    }
////                }
////            }
////        }
////        if(counter == 0) {
////            return null;
////        }
//        return potentialSuggestedTrips;
//    }
//
//    private void calcRoadTripsValues(List<RoadTrip> potentialSuggestedTrips) {
//        for(RoadTrip roadTrip : potentialSuggestedTrips) {
//            calcRoadTripValues(roadTrip);
//        }
//    }
//
//    private void calcRoadTripValues(RoadTrip roadTrip) {
//        roadTrip.calcTotalCost();
//        roadTrip.calcRequiredFuel();
//        roadTrip.calcStartArrivalTime();
//        roadTrip.buildRoadTripStory();
//    }
//
//    private void findPotentialMatchToArrivalTime(List<RoadTrip> potentialSuggestedTrips, int suggestedAmountTrips) {
////
//    }
//
//    private void findPotentialMatchesToStartTime(List<RoadTrip> potentialSuggestedTrips, int suggestedAmountTrips) {
//        Route[] routes = new Route[suggestedAmountTrips];
//        Route route1 = new Route();
//        route1.setPath("A,B");
//        Route route2 = new Route();
//        route2.setPath("B,C");
//        Route route3 = new Route();
//        route3.setPath("C,D");
//        routes[0] = route1;
//        routes[1] = route2;
//        routes[2] = route3;
//        int index=0;
//        RoadTrip currRoadTrip;
//        for(TripSuggest suggest : suggestedTrips) {
//            currRoadTrip = new RoadTrip();
//            currRoadTrip.setTripRequest(tripRequest);
//            currRoadTrip.addSuggestToRoadTrip(suggest, routes[index]);
//            potentialSuggestedTrips.add(currRoadTrip);
//            index++;
//        }
////        int counter = 0;
////        for (TripSuggest trip : suggestedTrips) {
////            if (checkIfTripIncludeSourceStation(trip)) { //trip suggest get to request start station
////                if (checkIfTripArrivalHourToRequestSourceStationIsEquals(trip, tripRequest)) {
////                    if (checkIfTripCanBeMatch(trip, tripRequest)) {
////                        counter++;
////                        if (counter == suggestedAmountTrips) {
////                            break;
////                            for (Map.Entry<TripSuggest, Integer> trip : suggestedTrips.entrySet()) {
////                                if (checkIFSuggestedTripIncludeRequestStations(requestTrip.getSourceStation(), requestTrip.getDestinationStation(), trip.getKey())) {
////                                    if ((!requestTrip.getIsStartTime() && requestTrip.getRequestRequiredTime() == trip.getKey().getArrivalHourToSpecificStation(requestTrip.getDestinationStation())) || (requestTrip.getIsStartTime() && checkRequestTimeToSuggestTrip(requestTrip, trip.getKey()))) {
////                                        if (trip.getKey().getRemainingCapacity() > 0) {
////                                            if (counter < suggestedAmountTrips) {
////                                                potentialSuggestedTrips[counter] = trip.getKey();
////                                                counter++;
////                                                if (counter == suggestedAmountTrips) {
////                                                    break;
////                                                }
////                                            }
////                                            //Create new RoadTrip with single trip suggest
////                                        } else {
////                                            String[] shortRoute = shortcutRootToRequestSourceDest(trip.getTripRoute(), tripRequest.getSourceStation());
////                                            handleShortRouteAction(potentialSuggestedTrips, tripRequest, trip, shortRoute, suggestedAmountTrips);
////                                        }
//////                                    }
//////                                }
//////                            }
////
////                        }
////                    }
////                }
////            }
////        }
//    }
//
//    private boolean checkIfTripCanBeMatch(TripSuggest tripSuggest, TripRequest requestTrip) {
//        String[] stations = tripSuggest.getTripRoute().split(",");
//        int indexOfSource = -1;
//        int indexOfDest = -1;
//
//        for(int i =0; i < stations.length; i++) {
//            if(stations[i].equals(requestTrip.getSourceStation())) {
//                indexOfSource = i;
//            }
//            if(stations[i].equals(requestTrip.getDestinationStation())) {
//                indexOfDest = i;
//            }
//        }
//
//        if(indexOfDest != -1 && indexOfSource != -1) {
//            return indexOfSource < indexOfDest;
//        }
//
//        return false;
//    }
//
//    private void handleShortRouteAction(List<RoadTrip> potentialSuggestedTrips, TripRequest requestTrip, TripSuggest suggestTrip, String[] shortRoute, int suggestedAmountTrips) {
//        for(int i = 1; i < shortRoute.length; i++) {
//
//        }
//        if(shortRoute.length == 2) {
//            if(shortRoute[1].equals(requestTrip.getDestinationStation())) {
//                //found
//            }
//            else {
//
//            }
//        }
//        else {
//
//        }
//    }
//
//    public boolean handleShortRouteAction(Map<TripSuggest, Integer> suggestedTrip, TripRequest tripRequest, String[] shortRoute) {
//        if(isCurrentShortRouteSatisfied(shortRoute, tripRequest)) {
//            return true;
//        }
//        else {
//            for(int i = 0; i < shortRoute.length; i++) {
//                TripSuggest[] suggestedTrips = findSuggestedTripsWithCurrStation(shortRoute[i]);
//                handleShortRouteAction(suggestedTrip, tripRequest, getShortRouteWithoutFirstElement(shortRoute));
//            }
//
//        }
//        return false;
//    }
//
//    private TripSuggest[] findSuggestedTripsWithCurrStation(String station) {
//        return null;
//    }
//
//    private String[] getShortRouteWithoutFirstElement(String[] shortRoute) {
//        int size = shortRoute.length - 1;
//        String[] ret = new String[size];
//
//        for(int i = 1; i < shortRoute.length; i++) {
//            ret[i-1] = shortRoute[i];
//        }
//        return ret;
//    }
//
//    private boolean isCurrentShortRouteSatisfied(String[] shortRoute, TripRequest tripRequest) {
//        for(int i = 0; i < shortRoute.length; i++) {
//            if(shortRoute[i].equals(tripRequest.getDestinationStation())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static String[] shortcutRootToRequestSourceDest(String tripRoute, String sourceStation) {
//        String[] stations = tripRoute.split(",");
//        String[] newRoute;
//        int index = 0;
//        boolean stationFound = false;
//
//        for(int i = 0; i < stations.length; i++) {
//            if(stations[i].equals(sourceStation)) {
//                index = i;
//                break;
//            }
//        }
//        newRoute = new String[stations.length - index];
//
//        for(int i = index, j = 0; i < stations.length; i++, j++) {
//            newRoute[j] = stations[i];
//        }
//
//        return newRoute;
//    }
//
//    public static void main(String[] args) {
//        String[] answer = shortcutRootToRequestSourceDest("A,B,C,D,E,F", "C");
//        System.out.println("");
//    }
//
//    private boolean checkIfSuggestedTripIncludeRequestDestStation(TripSuggest trip, String destStation, String sourceStation) {
//        String[] route = trip.getTripRoute().split(",");
//        for(int i = 0; i < route.length - 1; i++) {
//            if(route[i].equals(destStation)) {
//                int index = getSourceStationIndexInRoute(trip, sourceStation);
//                return i > index;
//            }
//        }
//        return false;
//    }
//
//    private int getSourceStationIndexInRoute(TripSuggest trip, String sourceStation) {
//        String[] route = trip.getTripRoute().split(",");
//        for(int i = 0; i < route.length - 1; i++) {
//            if(route[i].equals(sourceStation)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private boolean checkIfTripArrivalHourToRequestSourceStationIsEquals(TripSuggest trip, TripRequest tripRequest) {
//        //double arrivalTimeToSourceStation = trip.getArrivalHourToSpecificStation(tripRequest.getSourceStation());
//        //return arrivalTimeToSourceStation == tripRequest.getRequestRequiredTime();
//        return true;
//    }
//
//    private boolean checkIfTripIncludeSourceStation(TripSuggest trip) {
//        String[] route = trip.getTripRoute().split(",");
//        for(int i = 0; i < route.length - 1; i++) {
//            if(route[i].equals(tripRequest.getSourceStation())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean checkRequestTimeToSuggestTrip(TripRequest tripRequest, TripSuggest tripSuggest) {
//        //double hour = tripSuggest.getArrivalHourToSpecificStation(tripRequest.getSourceStation());
////       // if(hour == tripRequest.getRequestRequiredTime()) {
////           // return true;
////        //}
////        else {
//        return false;
////        }
//    }
//
//    private boolean checkIFSuggestedTripIncludeRequestStations(String sourceStation, String destinationStation, TripSuggest suggestedTrip) {
//        String suggestedTripRoute = suggestedTrip.getTripRoute();
//        String[] stations = suggestedTripRoute.split(",");
//        int indexOfSourceDestination = -1;
//        int indexOfDestinationStation = -1;
//
//        for(int i = 0; i < stations.length; i++) {
//            if(stations[i].equals(sourceStation)) {
//                indexOfSourceDestination = i;
//            }
//            if(stations[i].equals(destinationStation)) {
//                indexOfDestinationStation = i;
//            }
//        }
//        if(indexOfSourceDestination < indexOfDestinationStation && indexOfDestinationStation != -1 && indexOfSourceDestination != -1) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean checkIfSuggestedTripStartAtRequestDay(TripSuggest suggest) {
////        int requestRequiredDay = tripRequest.getTripDay();
////        int suggestStartingDay = suggest.getTripStartDay();
////        int temp;
////
////        if(requestRequiredDay < suggestStartingDay) {
////            return false;
////        }
////        else if(requestRequiredDay == suggestStartingDay) {
////            return true;
////        }
////        else {
////            switch (suggest.getRecurrencesType()) {
////                case ONE_TIME_ONLY: {
////                    if(requestRequiredDay != suggestStartingDay) {
////                        return false;
////                    }
////                    else {
////                        return true;
////                    }
////                }
////                case DAILY: {
////                    temp = suggestStartingDay;
////                    while(temp < requestRequiredDay) {
////                        if(temp == requestRequiredDay) {
////                            return true;
////                        }
////                        temp++;
////                    }
////                    return false;
////                }
////                case BI_DAILY: {
////                    temp = suggestStartingDay;
////                    while(temp < requestRequiredDay) {
////                        if(temp == requestRequiredDay) {
////                            return true;
////                        }
////                        temp+=2;
////                    }
////                    return false;
////
////                }
////                case WEEKLY: {
////                    temp = suggestStartingDay;
////                    while(temp < requestRequiredDay) {
////                        if(temp == requestRequiredDay) {
////                            return true;
////                        }
////                        temp+=7;
////                    }
////                    return false;
////
////                }
////                case MONTHLY: {
////                    temp = suggestStartingDay;
////                    while(temp < requestRequiredDay) {
////                        if(temp == requestRequiredDay) {
////                            return true;
////                        }
////                        temp+=30;
////                    }
////                    return false;
////
////                }
////
////            }
////        }
////
//                            return false;
//                        }

                    }
