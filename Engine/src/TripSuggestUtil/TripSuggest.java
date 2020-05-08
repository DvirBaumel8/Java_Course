package TripSuggestUtil;

import Manager.EngineManager;
import TripRequests.TripRequest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripSuggest {
    private int suggestID;
    private String TripOwnerName;
    private String tripRoute;
    private double arrivalDayNumber;
    private double startingHour;
    private String startingHourAsTime;
    private double arrivalHour;
    private String arrivalHourAsTime;
    private TripScheduleType tripScheduleType;
    private int ppk;
    private int remainingCapacity;
    private int tripPrice;

    private List<Integer> passengers;
    private List<StopStationDetails> stopStationsDetails;
    private int requiredFuel;

    public TripSuggest(String ownerName, Route route, int arrivalDayNumber, double startingHour, int tripScheduleTypeInt, int ppk, int driverCapacity) {
        this.TripOwnerName = ownerName;
        this.tripRoute = trimRoute(route);
        this.arrivalDayNumber = arrivalDayNumber;
        this.startingHour = startingHour;
        this.startingHourAsTime = EngineManager.getTime(startingHour);
        this.setTripScheduleTypeByInt(tripScheduleTypeInt);
        this.ppk = ppk;
        this.remainingCapacity = driverCapacity;
        this.tripPrice = calculateTripPrice(ppk, route);
        this.arrivalHour = calcArrivalHour(route.getPath());
        this.arrivalHourAsTime = EngineManager.getTime(arrivalHour);
        this.passengers = new ArrayList<>();
        this.stopStationsDetails = new ArrayList<>();
        this.requiredFuel = calcRequiredFuel(route);
    }

    private String trimRoute(Route route) {
        String[] stations = route.getPath().split(",");
        StringBuilder str = new StringBuilder();

        for(int i =0; i < stations.length; i++) {
            stations[i] = stations[i].trim();
        }

        for(int i = 0; i < stations.length ; i++) {
            str.append(stations[i]);
            if(i != stations.length - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }

    private double calcArrivalHour(String path) {
        double sum = this.startingHour;
        String[] paths = path.split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            sum = EngineManager.getEngineManagerInstance().calcArrivalHourToRoute(paths[i], paths[i+1], sum);
        }
        double fraction = sum % 1;
        if(fraction > 0.6) {
            sum = sum - 0.6;
            sum++;
        }
        fraction = (sum * 100) % 10;
        if( fraction != 5 && fraction != 0) {
            if(fraction > 2.5) {
                sum = sum - fraction / 100;
                sum = sum + 0.05;
            }
            else {
                sum = sum - fraction / 100;
            }
        }

        return sum;
    }


    private int calcRequiredFuel(Route route) {
      return TripSuggestUtil.calcRequiredFuel(route.getPath());
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

    public double getArrivalHour() {
        return arrivalHour;
    }

    public String getArrivalHourAsTime() {
        return arrivalHourAsTime;
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

    public double getStartingHour() {
        return startingHour;
    }

    public String getStartingHourAsTime() {
        return startingHourAsTime;
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
            index++;
        }

        if (index == 1) {
            str.setLength(0);
            str.append("No passengers\n");
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

    public void setSuggestID(int suggestIDID) {
        this.suggestID = suggestIDID;
    }

    enum TripScheduleType
    {
        ONE_TIME_ONLY, DAILY, BI_DAILY, WEEKLY, MONTHLY;
    }

    public void setTripScheduleTypeByInt(int tripScheduleType) {
        switch (tripScheduleType) {
            case 1:
            this.tripScheduleType = TripScheduleType.ONE_TIME_ONLY;
                break;
            case 2:
                this.tripScheduleType = TripScheduleType.DAILY;
                break;
            case 3:
                this.tripScheduleType = TripScheduleType.BI_DAILY;
                break;
            case 4:
                this.tripScheduleType = TripScheduleType.WEEKLY;
                break;
            case 5:
                this.tripScheduleType = TripScheduleType.MONTHLY;
                break;
        }
    }

    public double getArrivalHourToSpecificStation(String stationName) {
        String[] paths = tripRoute.split(",");
        StringBuilder pathToCalc = new StringBuilder();

        for(int i =0; i < paths.length; i++) {
            if(!paths[i].equals(stationName)) {
                pathToCalc.append(paths[i]);
                pathToCalc.append(",");
            }
            else {
                pathToCalc.append(paths[i]);
                break;
            }
        }
        return calcArrivalHour(pathToCalc.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripSuggest that = (TripSuggest) o;
        return suggestID == that.suggestID &&
                tripPrice == that.tripPrice &&
                startingHour == that.startingHour &&
                arrivalHour == that.arrivalHour &&
                remainingCapacity == that.remainingCapacity &&
                requiredFuel == that.requiredFuel &&
                TripOwnerName.equals(that.TripOwnerName) &&
                tripRoute.equals(that.tripRoute) &&
                passengers.equals(that.passengers) &&
                stopStationsDetails.equals(that.stopStationsDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestID, TripOwnerName, tripRoute, tripPrice, startingHour, arrivalHour, remainingCapacity, passengers, stopStationsDetails, requiredFuel);
    }
}
