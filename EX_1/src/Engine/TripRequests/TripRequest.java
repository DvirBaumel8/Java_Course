package Engine.TripRequests;

public class TripRequest {
    ￿private String nameOfOwner;
    private String sourceStation;
    private String destinationStation;
    private int startingHour;

    public TripRequest(String name, String sourceStation, String destinationStation, int startingHour) {
        this.nameOfOwner = name;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.startingHour = startingHour;
    }

}
