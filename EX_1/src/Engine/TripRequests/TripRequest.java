package Engine.TripRequests;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private int startingHour;
    private boolean isMatched;

    public TripRequest(String name, String sourceStation, String destinationStation, int startingHour) {
        this.OwnerName = name;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.startingHour = startingHour;
        this.isMatched = false;
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
}
