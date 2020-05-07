package TripSuggestUtil;

public class StopStationDetails {
    private String stationName;
    private String stopStationOwner;
    private boolean isUp;

    public StopStationDetails(String stationName, String stopStationOwner, boolean isUp) {
        this.stationName = stationName;
        this.stopStationOwner = stopStationOwner;
        this.isUp = isUp;
    }

    public String getStationName() {
        return stationName;
    }

    public String getStopStationOwner() {
        return stopStationOwner;
    }

    public boolean isUp() {
        return isUp;
    }
}
