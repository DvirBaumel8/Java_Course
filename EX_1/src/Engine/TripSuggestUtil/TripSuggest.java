package Engine.TripSuggestUtil;

import Engine.Manager.EngineManager;
import Engine.TripRequests.TripRequest;
import Engine.XMLLoading.jaxb.schema.generated.Route;
import Engine.XMLLoading.jaxb.schema.generated.Scheduling;

import java.util.ArrayList;
import java.util.List;

public class TripSuggest {
    private int suggestID;
    private String TripOwnerName;
    private String tripRoute;
    private int tripPrice;
    private int startingHour;
    private int arrivalHour;
    private int remainingCapacity;
    private List<Integer> passengers;
    private List<StopStationDetails> stopStationsDetails;
    private int requiredFuel;

    public TripSuggest(String ownerName, int capacity, int ppk, Route route, Scheduling scheduling, int ID) {
        this.TripOwnerName = ownerName;
        this.remainingCapacity = capacity;
        this.tripRoute = route.getPath();
        this.tripPrice = calculateTripPrice(ppk, route);
        this.startingHour = scheduling.getHourStart();
        this.arrivalHour = calcArrivalHour(route);
        this.passengers = new ArrayList<>();
        this.stopStationsDetails = new ArrayList<>();
        this.requiredFuel = calcRequiredFuel(route);
        this.suggestID = ID;
    }

    private int calcArrivalHour(Route route) {
        int sum = 0;
        String[] paths = route.getPath().split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            sum += EngineManager.getEngineManagerInstance().calcArrivalHourToRoute(paths[i], paths[i+1], this.startingHour);
        }
        return sum;
    }


    private int calcRequiredFuel(Route route) {
        int sum = 0;
        String[] paths = route.getPath().split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            sum += EngineManager.getEngineManagerInstance().getRequiredFuelToPath(paths[i], paths[i+1]);
        }
        return sum;
    }

    private int calculateTripPrice(int ppk, Route route) {
        int sum = 0;
        String[] paths = route.getPath().split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            int km = getLengthBetweenStations(paths[i], paths[i+1]);
            sum += km * ppk;
        }
        return sum;
    }

    public int getArrivalHour() {
        return arrivalHour;
    }

    private int getLengthBetweenStations(String pathFrom, String pathTo) {
        return EngineManager.getEngineManagerInstance().getLengthBetweenStations(pathFrom, pathTo);
    }

    public List<Integer> getPassengers() {
        return passengers;
    }

    public int getTripPrice() {
        return tripPrice;
    }

    public int getStartingHour() {
        return startingHour;
    }

    public String getTripOwnerName() {
        return TripOwnerName;
    }

    public String getTripRoute() {
        return tripRoute;
    }

    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    public int getRequiredFuel() {
        return requiredFuel;
    }

    public int getSuggestID() {
        return suggestID;
    }

    public String getStationsDetailsAsString () {
        StringBuilder str = new StringBuilder();
        str.append("All stop stations for passengers:\n");
        int index = 1;

        for(StopStationDetails stopDetails : stopStationsDetails) {
            str.append(String.format("%d:\n", index));
            str.append(String.format("Station name - %s\n", stopDetails.getStationName()));
            str.append(String.format("Passenger name - %s\n", stopDetails.getStopStationOwner()));
            if(stopDetails.isUp()) {
                str.append("Passenger is going up on this station\n\n");
            }
            else {
                str.append("Passenger is going down on this station\n\n");
            }
        }

        return str.toString();
    }

    public void addNewPassengerToTrip (TripRequest tripRequest) {
        remainingCapacity--;
        passengers.add(tripRequest.getRequestID());
        StopStationDetails sourceStationDetails = new StopStationDetails(tripRequest.getSourceStation(), tripRequest.getNameOfOwner(), true);
        StopStationDetails destinationStationDetails = new StopStationDetails(tripRequest.getDestinationStation(), tripRequest.getNameOfOwner(), false);
        stopStationsDetails.add(sourceStationDetails);
        stopStationsDetails.add(destinationStationDetails);
    }
}
