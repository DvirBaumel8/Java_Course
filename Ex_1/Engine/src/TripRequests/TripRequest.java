package TripRequests;

import Manager.EngineManager;
import TripSuggestUtil.TripSuggest;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private double arrivalHour;
    private String arrivalHourAsTime;
    private boolean isMatched;
    private TripSuggest matchTrip;

    public TripRequest(String name, String sourceStation, String destinationStation, double arrivalHour) {
        this.OwnerName = name;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.isMatched = false;
        this.matchTrip = null;
        this.arrivalHour = arrivalHour;
        this.arrivalHourAsTime = EngineManager.getTime(arrivalHour);
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

    public double getArrivalHour() {
        return arrivalHour;
    }

    public String getArrivalHourAsTime() {
        return arrivalHourAsTime;
    }
}
