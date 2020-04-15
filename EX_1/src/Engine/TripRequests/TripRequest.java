package Engine.TripRequests;

import Engine.TripSuggestUtil.TripSuggest;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private int startingHour;
    private boolean isMatched;
    private TripSuggest matchTrip;

    public TripRequest(String name, String sourceStation, String destinationStation, int startingHour) {
        this.OwnerName = name;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.startingHour = startingHour;
        this.isMatched = false;
        this.matchTrip = null;
    }

    public String getNameOfOwner() {
        return OwnerName;
    }


    public boolean isMatched() {
        return isMatched;
    }

    public int getStartingHour() {
        return startingHour;
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
}
