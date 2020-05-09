package TripRequests;

import Manager.EngineManager;
import TripSuggestUtil.TripSuggest;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private double requiredTime;
    private String arrivalHourAsTime;
    private boolean isMatched;
    private TripSuggest matchTrip;
    private boolean requestByStartTime;

    public TripRequest(String name, String sourceStation, String destinationStation, double time, boolean requestByStartTime) {
        this.OwnerName = name;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.isMatched = false;
        this.matchTrip = null;
        this.requiredTime = time;
        this.arrivalHourAsTime = EngineManager.convertDoubleTimeToStrTime(time);
        this.requestByStartTime = requestByStartTime;
    }

    public String getNameOfOwner() {
        return OwnerName;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public int getRequestID() {
        return RequestID;
    }

    public void setRequestID(int requestID) {
        RequestID = requestID;
    }

    public TripSuggest getMatchTrip () {
        return matchTrip;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public void setMatchTrip(TripSuggest matchTrip) {
        this.matchTrip = matchTrip;
    }

    public double getRequestRequiredTime() {
        return requiredTime;
    }

    public String getTimeStr() {
        return arrivalHourAsTime;
    }

    public boolean isRequestByStartTime() {
        return requestByStartTime;
    }
}
