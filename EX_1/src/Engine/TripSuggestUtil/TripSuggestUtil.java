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
            TripSuggest tripSuggest = new TripSuggest(trip.getOwner(), trip.getCapacity(), trip.getPPK(), trip.getRoute(), trip.getScheduling(), nextSuggestID);
            suggestedTrips.put(tripSuggest, nextSuggestID);
            nextSuggestID++;
        }
    }

    public Map<TripSuggest, Integer> getAllSuggestedTrips () {
        return suggestedTrips;
    }
}
