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
                    combinedTrip.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index], entry.getValue().getRide()[index + 1], findClosestDayFromAbove(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], request.getStartTime().getDay(), request.getStartTime().getHours(), request.getStartTime().getMinutes())));
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
                    matchingRides.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index], entry.getValue().getRide()[index + 1], findClosestDayFromAbove(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], ride.getLast().getLastStation().getDay(), ride.getLast().getLastStation().getHour(), ride.getLast().getLastStation().getMinutes())));
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
                    combinedTrip.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index - 1], entry.getValue().getRide()[index], findClosestDayFromBelow(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], request.getArrivalTime().getDay(), request.getArrivalTime().getHours(), request.getArrivalTime().getMinutes())));
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
                    matchingRides.add(new SubTrip(entry.getValue(), entry.getValue().getRide()[index - 1], entry.getValue().getRide()[index], findClosestDayFromBelow(entry.getValue(), entry.getValue().getRecurrencesType().getValue(), entry.getValue().getRide()[index], ride.getFirst().getRoute().getFirst().getDay(), ride.getFirst().getRoute().getFirst().getHour(), ride.getFirst().getRoute().getFirst().getMinutes())));
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

}
