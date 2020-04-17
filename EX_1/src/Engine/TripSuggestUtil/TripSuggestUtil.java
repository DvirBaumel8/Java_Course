package Engine.TripSuggestUtil;

import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripSuggestUtil {
    private int nextSuggestID;
    private Map<TripSuggest, Integer> suggestedTrips;

    public TripSuggestUtil() {
        this.nextSuggestID = 1;
        this.suggestedTrips = new HashMap<>();
    }

    public Integer getTripID(TripSuggest trip) {
        return suggestedTrips.get(trip);
    }

    public void convertPlannedTripsToSuggestedTrips (List<TransPoolTrip> plannedTrips) {
        for(TransPoolTrip trip : plannedTrips) {
            int ppk =  Integer.valueOf(trip.getPPK());
            int tripScheduleTypeInt = getTripScheduleTypeInt(trip.getScheduling().getRecurrences());
            TripSuggest tripSuggest = new TripSuggest(trip.getOwner(), trip.getRoute(),
                    trip.getScheduling().getHourStart(), trip.getScheduling().getDayStart(), tripScheduleTypeInt, ppk,
                    trip.getCapacity());
            suggestedTrips.put(tripSuggest, nextSuggestID);
            nextSuggestID++;
        }
    }

    public void addSuggestTrip(TripSuggest suggestTrip) {
        suggestTrip.setSuggestID(nextSuggestID);
        suggestedTrips.put(suggestTrip, nextSuggestID);
        nextSuggestID++;
    }

    public Map<TripSuggest, Integer> getAllSuggestedTrips () {
        return suggestedTrips;
    }

    int getTripScheduleTypeInt (String tripScheduleType) {
        int res = 0;
        switch(tripScheduleType)
        {//here need to change t
            case "One time":
                res = 1;
                break;
            case "Daily":
                res = 2;
                break;
            case "Bi day":
                res = 3;
                break;
            case "Weekly":
                res = 4;
                break;
            case "Monthly":
                res = 5;
                break;
        }

        return res;
    }
}
