package Engine.TripRequests;

import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.util.ArrayList;
import java.util.List;

public class TripRequestsUtil {
    private List<TripRequest> requestTrips;

    public TripRequestsUtil() {
        requestTrips = new ArrayList<>();
    }

    public void addRequestTrip(TripRequest requestTrip) {
        requestTrips.add(requestTrip);
    }
}
