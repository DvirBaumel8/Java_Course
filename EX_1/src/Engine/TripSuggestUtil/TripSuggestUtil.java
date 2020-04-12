package Engine.TripSuggestUtil;

import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.util.HashMap;
import java.util.Map;

public class TripSuggestUtil {
    private int nextSuggestID;
    private Map<TransPoolTrip, Integer> suggestedTrips;

    public TripSuggestUtil() {
        this.nextSuggestID = 1;
        this.suggestedTrips = new HashMap<>();
    }

    public Integer getTripID(TransPoolTrip trip) {
        return suggestedTrips.get(trip);
    }
}
