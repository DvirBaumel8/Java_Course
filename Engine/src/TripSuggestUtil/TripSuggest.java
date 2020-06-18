package TripSuggestUtil;

import Manager.EngineManager;
import MatchingUtil.Station;
import Time.Time;
import TripRequests.TripRequest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TripSuggest {
    private int suggestID;
    private String TripOwnerName;
    private Route tripRoute;
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
    private Station[] stations;

    public TripSuggest(String ownerName, Route route, int minutes, int hour, int day, int recurrencesType, int ppk, int driverCapacity) {
        this.TripOwnerName = ownerName;
        this.tripRoute = route;
        handleRoute();
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
        calcStationsArrivalHour();
    }

    private void calcStationsArrivalHour() {
        for(int i = 0; i < stations.length; i++) {
            calcArrivalHourToSpecificStation(stations[i]);
        }
    }

    private void handleRoute() {
        String[] elements = tripRoute.getPath().split(",");
        stations = new Station[elements.length];

        for(int i =0; i < stations.length; i++) {
            stations[i] = new Station(elements[i]);
        }
    }

    public int getPpk() {
        return ppk;
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

    public Route getTripRoute() {
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

    public Station getFirstStation() {
        return stations[0];
    }

    public Station getLastStation() {
        return stations[stations.length - 1];
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

    public Time getArrivalTimeToStation(Station stationName) {
        for(int i = 0; i < stations.length; i++) {
            if(stationName.equals(stations[i])) {
                return stations[i].getTime();
            }
        }
        return null;
    }

    public void calcArrivalHourToSpecificStation(Station station) {
        StringBuilder pathToCalc = new StringBuilder();

        for(int i =0; i < stations.length; i++) {
            if(!stations[i].getName().equals(station.getName())) {
                pathToCalc.append(stations[i]);
                pathToCalc.append(",");
            }
            else {
                pathToCalc.append(stations[i]);
                break;
            }
        }
        station.setArrivalTime(calcArrivalHour(pathToCalc.toString()));
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

    public Time getStartingTime() {
        return startingTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public int getStartingDay() {
        return startingDay;
    }

    public Station[] getRide() {
        return stations;
    }

    public Station[] getTripStations() {
        return stations;
    }
}
