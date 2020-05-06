package TripRequests;

import TripSuggestUtil.TripSuggest;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private double arrivalHour;
    private boolean isMatched;
    private TripSuggest matchTrip;

    public TripRequest(String name, String sourceStation, String destinationStation,int arrivalHour) {
        this.OwnerName = name;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.isMatched = false;
        this.matchTrip = null;
        this.arrivalHour = arrivalHour;
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

    public void matchRequestToTrip (TripSuggest tripSuggest) {
        isMatched = true;
        matchTrip = tripSuggest;
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
}
