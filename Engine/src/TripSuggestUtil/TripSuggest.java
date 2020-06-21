package TripSuggestUtil;

import Manager.EngineManager;
import MatchingUtil.RoadTrip;
import MatchingUtil.Station;
import MatchingUtil.SubTrip;
import Time.Time;
import TripRequests.TripRequest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripSuggest {
    private int suggestID;
    private String TripOwnerName;
    private Route tripRoute;
    private RecurrencesTypes recurrencesType;
    private int ppk;
    private int staticCapacity;
    private Map<Integer,Integer> capacityPerTime;
    private int tripPrice;
    private DriverRating driverRating;
    private List<Integer> passengers;
    private int requiredFuel;
    private Time startingTime;
    private Time arrivalTime;
    private int startingDay;
    private Station[] stations;
    private boolean isActive;
    private List<String> liveParticipantsTripName;

    public TripSuggest(String ownerName, Route route, int minutes, int hour, int day, int recurrencesType, int ppk, int driverCapacity) {
        this.TripOwnerName = ownerName;
        this.tripRoute = route;
        handleRoute();
        this.setTripScheduleTypeByInt(recurrencesType);
        this.ppk = ppk;
        this.startingTime = new Time(minutes, hour, day);
        this.startingDay = day;
        this.staticCapacity = driverCapacity;
        this.tripPrice = calculateTripPrice(ppk, route);
        this.passengers = new ArrayList<>();
        this.requiredFuel = calcRequiredFuel(route);
        this.driverRating = new DriverRating();
        this.arrivalTime = calcArrivalHour(route.getPath());
        this.capacityPerTime = new HashMap<>();
        calcStationsArrivalHour();
        updateIsActive(EngineManager.getEngineManagerInstance().getCurrentSystemTime());
        updateCapacityPerTime(EngineManager.getEngineManagerInstance().getCurrentSystemTime());
        this.liveParticipantsTripName = new ArrayList<>();
    }

    public void updateCapacityPerTime(Time currTime) {
        int totalCurrTimeMinutes = getTotalMinutesToTime(currTime);
        if(capacityPerTime.containsKey(totalCurrTimeMinutes)) {
            return;
        }
        else {

        }
        if(isActive) {
            liveParticipantsTripName.clear();
            //liveCapacity = staticCapacity;
            List<TripRequest> matchedTripRequests = EngineManager.getEngineManagerInstance().getAllMatchedTripRequests();
            RoadTrip roadTrip;
            for(TripRequest request : matchedTripRequests) {
                roadTrip = request.getMatchTrip();
                for(SubTrip subTrip : roadTrip.getSubTrips()) {
                    if(subTrip.getTrip().getSuggestID() == suggestID) {
                        if(TripSuggestUtil.compareTimes(currTime, startingTime) == 1 && TripSuggestUtil.compareHours(currTime, arrivalTime) == 3) {
                            //liveCapacity--;
                            liveParticipantsTripName.add(request.getNameOfOwner());
                        }
                    }
                }
            }

        }
    }

    private int getTotalMinutesToTime(Time currTime) {
        int day = currTime.getDay();
        int hours = currTime.getHours();
        int minutes = currTime.getMinutes();

        return (day * 24 * 60) + (hours * 60) + (minutes);
    }

    public static void main(String[] args) throws Exception {
        Route route = new Route();
        route.setPath("A,B,C");

        List<String> errors = new ArrayList<>();
        EngineManager.getEngineManagerInstance().LoadXML("/Users/db384r/Dev/Java/Test files/EX_2/ex2-small-empty.xml", errors);
        TripSuggest tripSuggest = new TripSuggest("Dvir", route, 0,8,12, 4,30,3);
        EngineManager.getEngineManagerInstance().addNewTripSuggest(tripSuggest);
        TripRequest request = new TripRequest("Gal", "B", "C", 15, 8, 12, true);
        EngineManager.getEngineManagerInstance().addNewTripRequest(request);
        EngineManager.getEngineManagerInstance().findPotentialSuggestedTripsToMatch("1,1");
        EngineManager.getEngineManagerInstance().matchTripRequest("1", "1,1");

        for(int i =0; i < 18; i++) {
            EngineManager.getEngineManagerInstance().moveTimeForward(5);
        }
        EngineManager.getEngineManagerInstance().moveTimeForward(4);
        EngineManager.getEngineManagerInstance().moveTimeForward(4);
        EngineManager.getEngineManagerInstance().moveTimeForward(4);
        EngineManager.getEngineManagerInstance().moveTimeForward(4);
        EngineManager.getEngineManagerInstance().moveTimeForward(1);
        EngineManager.getEngineManagerInstance().moveTimeForward(1);
        EngineManager.getEngineManagerInstance().moveTimeForward(1);
        EngineManager.getEngineManagerInstance().moveTimeForward(1);
        EngineManager.getEngineManagerInstance().moveTimeForward(1);
        EngineManager.getEngineManagerInstance().moveTimeForward(1);
       // Map<TripSuggest, String> map = EngineManager.getEngineManagerInstance().getMapDetailsPerTime();

        System.out.println("");
    }

    public void updateIsActive(Time currTime) {
        if (recurrencesType.getValue() == 1) {
            if (TripSuggestUtil.compareTimes(startingTime, currTime) == 2) {
                isActive = true;
                return;
            } else if (TripSuggestUtil.compareTimes(currTime, startingTime) == 1 && (TripSuggestUtil.compareTimes(currTime, arrivalTime) == 3) || TripSuggestUtil.compareTimes(currTime, arrivalTime) == 2) {
                isActive = true;
                return;
            }
            isActive = false;
        } else {
            Time tripTempTime = startingTime;
            int tempDay = tripTempTime.getDay();
            int tripTotalMinutesT = (tempDay * 24 * 60) + (tripTempTime.getHours() * 60) + (tripTempTime.getMinutes());
            int currTotalMinute = (currTime.getDay() * 24 * 60) + (currTime.getHours() * 60) + (currTime.getMinutes());

            while (TripSuggestUtil.compareTimes(currTotalMinute, tripTotalMinutesT) == 1) {
                tempDay += recurrencesType.getValue();
                tripTotalMinutesT = (tempDay * 24 * 60) + (tripTempTime.getHours() * 60) + (tripTempTime.getMinutes());
            }
            Time timeWithRec = new Time(startingTime.getMinutes(), startingTime.getHours(), tempDay);
            if (TripSuggestUtil.compareTimes(timeWithRec, currTime) == 2) {
                isActive = true;
                return;
            } else if (TripSuggestUtil.compareTimes(currTime, startingTime) == 1 && (TripSuggestUtil.compareTimes(currTime, arrivalTime) == 3) || TripSuggestUtil.compareTimes(currTime, arrivalTime) == 2) {
                isActive = true;
                return;
            }
        }

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

    public int getstaticCapacity() {
        return staticCapacity;
    }

    public int getRequiredFuel() {
        return requiredFuel;
    }

    public int getSuggestID() {
        return suggestID;
    }

    public void setSuggestID(int suggestIDID) {
        this.suggestID = suggestIDID;
    }

    public Map<Integer,Integer> getCapacityPerTime() {
        return capacityPerTime;
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
                pathToCalc.append(stations[i].getName());
                pathToCalc.append(",");
            }
            else {
                pathToCalc.append(stations[i].getName());
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

    public boolean isActive() {
        return isActive;
    }

    public void addNewItemToCapacityPerTimeMap(int totalMinutes) {
        if(this.capacityPerTime.containsKey(totalMinutes)) {
            Integer currCapacity = this.capacityPerTime.get(totalMinutes);
            capacityPerTime.put(totalMinutes, capacityPerTime.get(totalMinutes) - 1);
        }
        else {
            capacityPerTime.put(totalMinutes, staticCapacity - 1);
        }

    }

}
