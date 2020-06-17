package TripSuggestUtil;

import Manager.EngineManager;
import Time.Time;
import TripRequests.TripRequest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TripSuggest {
    private int suggestID;
    private String TripOwnerName;
    private String tripRoute;
    private RecurrencesTypes recurrencesType;
    private int ppk;
    private int remainingCapacity;
    private int tripPrice;
    private DriverRating driverRating;
    private List<Integer> passengers;
    private List<StopStationDetails> stopStationsDetails;
    private int requiredFuel;
    private Time startingTime;
    private Time arrivalTime;
    private int startingDay;

    public TripSuggest(String ownerName, Route route, int minutes, int hour, int day, int recurrencesType, int ppk, int driverCapacity) {
        this.TripOwnerName = ownerName;
        this.tripRoute = trimRoute(route);
        this.setTripScheduleTypeByInt(recurrencesType);
        this.ppk = ppk;
        this.startingTime = new Time(minutes, hour, day);
        this.startingDay = day;
        this.remainingCapacity = driverCapacity;
        this.tripPrice = calculateTripPrice(ppk, route);
        this.passengers = new ArrayList<>();
        this.stopStationsDetails = new ArrayList<>();
        this.requiredFuel = calcRequiredFuel(route);
        this.driverRating = new DriverRating();
        this.arrivalTime = calcArrivalHour(route.getPath());
    }

    public int getPpk() {
        return ppk;
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

    private Time calcArrivalHour(String path) {
        int sumMinutes = 0;
        double currRouteMinutes = 0;

        String[] paths = path.split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            currRouteMinutes = EngineManager.getEngineManagerInstance().calcMinutesToRoute(paths[i], paths[i+1]);
            sumMinutes += convertMinutesToHoursFormat(currRouteMinutes);
        }
        Time arrivalTime = new Time(startingTime.getMinutes(), startingTime.getHours(), startingTime.getDay());
        arrivalTime.addMinutes(sumMinutes);
        return arrivalTime;
    }

    private int convertMinutesToHoursFormat(double currRouteMinutes) {
        int retVal = 0;
        double temp;

        currRouteMinutes = currRouteMinutes * 0.6;

        temp = (currRouteMinutes * 100) % 10;

        if( temp != 5.0 && temp != 0) {
            temp = temp % 5;
            if(temp > 2.5) {
                currRouteMinutes = currRouteMinutes - temp/100;
                currRouteMinutes = currRouteMinutes + 0.05;
            }
            else {
                currRouteMinutes = currRouteMinutes - temp/100;
            }
        }
        retVal += currRouteMinutes*100;
        return retVal;
    }

    public int calcRequiredFuel(Route route) {
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

    private int getLengthBetweenStations(String pathFrom, String pathTo) {
        return EngineManager.getEngineManagerInstance().getLengthBetweenStations(pathFrom, pathTo);
    }

    public List<Integer> getPassengers() {
        return passengers;
    }

    public int getTripPrice() {
        return tripPrice;
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

    public Map<Object, Integer> getCapacityPerTime() {
        return null;
    }

    public enum RecurrencesTypes
    {
        ONE_TIME_ONLY(0),
        DAILY(1),
        BI_DAILY(2),
        WEEKLY(7),
        MONTHLY(30);

        private final int value;
        RecurrencesTypes(int id) { this.value = id; }
        public int getValue() { return value; }

        public String getTripScheduleTypeString(){
            return this.toString();
        }
    }

    private void setTripScheduleTypeByInt(int tripScheduleType) {
        switch (tripScheduleType) {
            case 1:
                this.recurrencesType = RecurrencesTypes.ONE_TIME_ONLY;
                break;
            case 2:
                this.recurrencesType = RecurrencesTypes.DAILY;
                break;
            case 3:
                this.recurrencesType = RecurrencesTypes.BI_DAILY;
                break;
            case 4:
                this.recurrencesType = RecurrencesTypes.WEEKLY;
                break;
            case 5:
                this.recurrencesType = RecurrencesTypes.MONTHLY;
                break;
        }
    }

    public Time getArrivalHourToSpecificStation(String stationName) {
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

    public void addRatingToDriver(int rating) {
        driverRating.addOneToNumOfRatings();
        driverRating.addRatingToDriver(rating);
    }

    public void addRatingToDriver(int rating, String literalRating) {
        driverRating.addRatingToDriver(rating, literalRating);
    }

    public DriverRating getDriverRating() {
        return driverRating;
    }

    public RecurrencesTypes getRecurrencesType() {
        return recurrencesType;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        TripSuggest that = (TripSuggest) o;
//        return suggestID == that.suggestID &&
//                tripPrice == that.tripPrice &&
//                startingHour == that.startingHour &&
//                arrivalHour == that.arrivalHour &&
//                remainingCapacity == that.remainingCapacity &&
//                requiredFuel == that.requiredFuel &&
//                TripOwnerName.equals(that.TripOwnerName) &&
//                tripRoute.equals(that.tripRoute) &&
//                passengers.equals(that.passengers) &&
//                stopStationsDetails.equals(that.stopStationsDetails);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(suggestID, TripOwnerName, tripRoute, tripPrice, startingHour, arrivalHour, remainingCapacity, passengers, stopStationsDetails, requiredFuel);
//    }


    public Time getStartingTime() {
        return startingTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public int getStartingDay() {
        return startingDay;
    }
}
