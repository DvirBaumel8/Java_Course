package TripRequests;

import java.util.HashMap;
import java.util.Map;

public class TripRequestsUtil {
    private Map<TripRequest, Integer> requestTrips;

    private static final String validationSuccessMessage = "Trip request added successfully\n";
    private int nextRequestID;

    public TripRequestsUtil() {
        this.requestTrips = new HashMap<>();
        this.nextRequestID = 1;
    }

    public Map<TripRequest, Integer> getAllRequestTrips() {
        return requestTrips;
    }

    public void addRequestTrip(TripRequest requestTrip) {
        requestTrip.setRequestID(nextRequestID);
        requestTrips.put(requestTrip, nextRequestID);
        nextRequestID++;
    }

    public Integer getTripID(TripRequest trip) {
        return requestTrips.get(trip);
    }

    public String getValidationSuccessMessage () {
        return validationSuccessMessage;
    }

    public boolean isRequestIDExist(Integer requestID) {
        return requestTrips.containsValue(requestID);
    }

    public TripRequest getTripRequestByID(int requestID) {
        for(Map.Entry<TripRequest, Integer> trip : requestTrips.entrySet()) {
            if(trip.getValue() == requestID) {
                return trip.getKey();
            }
        }
        return null;
    }
}
