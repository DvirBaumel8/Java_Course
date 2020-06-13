package TripSuggestUtil;

import Manager.EngineManager;
import XML.XMLLoading.jaxb.schema.generated.TransPoolTrip;

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
            int ppk = trip.getPPK();
            TripSuggest tripSuggest = new TripSuggest(trip.getOwner(), trip.getRoute(), 0, trip.getScheduling().getHourStart(), trip.getScheduling().getDayStart(), getTripScheduleTypeInt(trip.getScheduling().getRecurrences()), ppk, trip.getCapacity());
            addSuggestTrip(tripSuggest);
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

    int getTripScheduleTypeInt(String tripScheduleType) {
        int res = 0;
        switch(tripScheduleType)
        {
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

    public static int calcRequiredFuel(String route) {
        int sum = 0;
        String[] paths = route.split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            sum += EngineManager.getEngineManagerInstance().getRequiredFuelToPath(paths[i], paths[i+1]);
        }
        return sum;
    }

    public TripSuggest getTripSuggestByID(int suggestID) {
        for(Map.Entry<TripSuggest, Integer> trip : suggestedTrips.entrySet()) {
            if(trip.getValue() == suggestID) {
                return trip.getKey();
            }
        }
        return null;
    }
}
