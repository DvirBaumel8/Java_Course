package TripRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripRequestsUtil {
    private Map<Integer, TripRequest> requestTrips;

    private int nextRequestID;

    public TripRequestsUtil() {
        this.requestTrips = new HashMap<>();
        this.nextRequestID = 1;
    }

    public List<TripRequest> getAllMatchedTripRequests() {
        List<TripRequest> matchedRequests = new ArrayList<>();

        for(Map.Entry<Integer, TripRequest> entry : requestTrips.entrySet()) {
            if(entry.getValue().isMatched()) {
                matchedRequests.add(entry.getValue());
            }
        }
        return matchedRequests;
    }

    public Map<Integer, TripRequest> getAllRequestTrips() {
        return requestTrips;
    }

    public void addRequestTrip(TripRequest requestTrip) {
        requestTrip.setRequestID(nextRequestID);
        requestTrips.put(nextRequestID, requestTrip);
        nextRequestID++;
    }

    public boolean isRequestIDExist(Integer requestID) {
        return requestTrips.containsKey(requestID);
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
        for(Map.Entry<Integer, TripRequest> trip : requestTrips.entrySet()) {
            if(trip.getKey() == requestID) {
                return trip.getValue();
            }
        }
        return null;
    }

    public List<String> getAllMatchedTripRequestAsString() {
        List<String> retVal = new ArrayList<>();

        for(Map.Entry<Integer, TripRequest> entry : requestTrips.entrySet()) {
            if(entry.getValue().isMatched()) {
                retVal.add(String.format("Trip ID - %d, Owner name - %s", entry.getValue().getRequestID(), entry.getValue().getNameOfOwner()));
            }
        }
        if(retVal.size() ==0) {
            retVal.add("System didn't find a matched trips request");
        }
        return retVal;
    }

    public List<TripRequest> getAllMatchedTripRequest() {
        List<TripRequest> retVal = new ArrayList<>();

        for(Map.Entry<Integer,TripRequest> entry : requestTrips.entrySet()) {
            if(entry.getValue().isMatched()) {
                retVal.add(entry.getValue());
            }
        }
        return retVal;
    }

    public List<String> getAllUnmatchedRequests() {
        List<String> retVal = new ArrayList<>();

        for(Map.Entry<Integer,TripRequest> entry : requestTrips.entrySet()) {
            if(!entry.getValue().isMatched()) {
                retVal.add(String.format("Request ID: %s, Trip Owner Name: %s", entry.getValue().getRequestID(),
                        entry.getValue().getNameOfOwner() + System.lineSeparator()));
            }
        }
        return retVal;
    }
}
