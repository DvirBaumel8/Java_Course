package TripRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public boolean isRequestIDExistInMatchedRequestTrips(Integer requestID) {
        List<TripRequest> matchedRequestTrips =  getAllMatchedTripRequest();

        for(TripRequest request : matchedRequestTrips) {
            if(request.getRequestID() == requestID) {
                return true;
            }
        }
        return false;
    }

    public TripRequest getTripRequestByID(int requestID) {
        for(Map.Entry<TripRequest, Integer> trip : requestTrips.entrySet()) {
            if(trip.getValue() == requestID) {
                return trip.getKey();
            }
        }
        return null;
    }

    public List<String> getAllMatchedTripRequestAsString() {
        List<String> retVal = new ArrayList<>();

        for(Map.Entry<TripRequest, Integer> entry : requestTrips.entrySet()) {
            if(entry.getKey().isMatched()) {
                retVal.add(String.format("Trip ID - %d, Owner name - %s", entry.getKey().getRequestID(), entry.getKey().getNameOfOwner()));
            }
        }
        if(retVal.size() ==0) {
            retVal.add("System didn't find a matched trips request");
        }
        return retVal;
    }

    public List<TripRequest> getAllMatchedTripRequest() {
        List<TripRequest> retVal = new ArrayList<>();

        for(Map.Entry<TripRequest, Integer> entry : requestTrips.entrySet()) {
            if(entry.getKey().isMatched()) {
                retVal.add(entry.getKey());
            }
        }
        return retVal;
    }
}
