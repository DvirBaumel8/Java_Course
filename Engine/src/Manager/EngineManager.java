package Manager;

import DateSystem.DateSystemManger;
import MatchingUtil.MatchingUtil;
import MatchingUtil.RoadTrip;
import Time.Time;
import Time.TimeManager;
import TripSuggestUtil.TripSuggestUtil;
import Validations.TripValidations.Validator;
import TripRequests.TripRequest;
import TripRequests.TripRequestsUtil;
import XML.SchemaBasedJAXBMain;
import TripSuggestUtil.TripSuggest;
import XML.XMLLoading.jaxb.schema.generated.TransPoolTrip;
import XML.XMLValidations.XMLValidationsImpl;
import XML.XMLLoading.jaxb.schema.generated.Path;
import XML.XMLLoading.jaxb.schema.generated.Route;
import XML.XMLLoading.jaxb.schema.generated.Stop;
import XML.XMLLoading.jaxb.schema.generated.TransPool;

import java.io.FileNotFoundException;
import java.util.*;

public class EngineManager {
    private static EngineManager engineManagerInstance;
    private TransPool transPool;
    private static TripRequestsUtil tripRequestUtil;
    private static TripSuggestUtil tripSuggestUtil;
    private static MatchingUtil matchingUtil;
    private static Validator validator;
    private static List<String> suggestTripOwners;
    private static Map<TripRequest, RoadTrip> matches;

    private static DateSystemManger dateSystemManger = null;

    public DateSystemManger getDateSystemManger() {
        return dateSystemManger;
    }

    public TripRequestsUtil getTripRequestUtil() {
        return tripRequestUtil;
    }

    public TripSuggestUtil getTripSuggestUtil() {
        return tripSuggestUtil;
    }


    public void setDateSystemManger(DateSystemManger dateSystemManger) {
        this.dateSystemManger = dateSystemManger;
    }

    private List<String> menuOrderErrorMessage;
    private static final String SUCCESS_MATCHING = "Your trip request was match to trip suggested successfully\n";

    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManagerInstance == null) {
            engineManagerInstance = new EngineManager();
            tripSuggestUtil = new TripSuggestUtil();
            tripRequestUtil = new TripRequestsUtil();
            validator = Validator.getInstance();
            matches = new HashMap<>();
        }
        return engineManagerInstance;
    }

    public List<String> LoadXML(String pathToTheXMLFile, List<String> errors) {
        SchemaBasedJAXBMain jax = new SchemaBasedJAXBMain();
        try {
            transPool = jax.init(pathToTheXMLFile);
        } catch (FileNotFoundException ex) {
            errors.add("No such file or directory\n");
            return errors;
        }

        XMLValidationsImpl xmlValidator = new XMLValidationsImpl(transPool);
        if (xmlValidator.validateXmlFile(errors, pathToTheXMLFile)) {
            try {
                tripSuggestUtil.convertPlannedTripsToSuggestedTrips(transPool.getPlannedTrips().getTransPoolTrip());
                findAllPlannedTripsOwnerNames();
            } catch (NullPointerException e) {
                suggestTripOwners = new ArrayList<>();
            }
        }
        return errors;
    }

    private void findAllPlannedTripsOwnerNames() {
        suggestTripOwners = new ArrayList<>();

        for (TransPoolTrip trip : transPool.getPlannedTrips().getTransPoolTrip()) {
            suggestTripOwners.add(trip.getOwner());
        }
    }

    public String getAllStationsName() {
        StringBuilder str = new StringBuilder();
        str.append("All stations names: \n");
        int index = 1;
        for (Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            str.append(String.format("%d- %s\n", index, stop.getName()));
            index++;
        }
        return str.toString();
    }

    public String getAllSuggestedTrips() {
        StringBuilder str = new StringBuilder();
        if (tripSuggestUtil.getAllSuggestedTrips().size() > 0) {
            str.append("All suggested trips: \n");
        } else {
            str.append("There are no suggested trips");
            return str.toString();
        }

        int index = 1;
        for (Map.Entry<TripSuggest, Integer> trip : tripSuggestUtil.getAllSuggestedTrips().entrySet()) {
            str.append(String.format("%d- \n", index));
            index++;
            str.append(String.format("Trip ID- %d\n", getSuggestTripID(trip.getKey())));
            str.append(String.format("Trip Owner- %s\n", trip.getKey().getTripOwnerName()));
            str.append(String.format("Trip Route- %s\n", trip.getKey().getTripRoute()));
            str.append(String.format("Trip Price- %s\n", trip.getKey().getTripPrice()));
            str.append(String.format("Trip starting hour- %s\nTrip arrival hour - %s\n", trip.getKey().getStartingTime().toString(), trip.getKey().getArrivalTime().toString()));
            str.append(String.format("Trip available sits- %s\n", trip.getKey().getRemainingCapacity()));
            str.append(String.format("Exists passengers trip:\n %s\n", getListOfAllTripPassengersID(trip.getKey())));
            str.append(String.format("Required fuel to trip- %s.L\n", trip.getKey().getRequiredFuel()));
            str.append(String.format("Stations to stop-  \n%s\n", trip.getKey().getStationsDetailsAsString()));
        }
        return str.toString();
    }

    public HashSet<String> getAllLogicStationsName() {
        HashSet<String> hashSet = new HashSet<>();
        List<Stop> stops = transPool.getMapDescriptor().getStops().getStop();
        for (Stop stop : stops) {
            hashSet.add(stop.getName());
        }
        return hashSet;
    }

    private String getListOfAllTripPassengersID(TripSuggest trip) {
        StringBuilder str = new StringBuilder();
        int index = 1;

        for (Integer id : trip.getPassengers()) {
            str.append(String.format("%d: Passenger ID - %d\n", index, id));
            index++;
        }

        if (index == 1) {
            str.append("empty");
        }
        return str.toString();
    }

    private Integer getSuggestTripID(TripSuggest trip) {
        return tripSuggestUtil.getTripID(trip);
    }

    public String getAllTripRequests() {
        StringBuilder str = new StringBuilder();
        if (tripRequestUtil.getAllRequestTrips().size() > 0) {
            str.append("All requested trips:\n");
        } else {
            str.append("There are no trip requests\n");
            return str.toString();
        }

        for (Map.Entry<TripRequest, Integer> trip : tripRequestUtil.getAllRequestTrips().entrySet()) {
            str.append(String.format("Trip ID- %d\n", getRequestTripID(trip.getKey())));
            str.append(String.format("Trip requester- %s\n", trip.getKey().getNameOfOwner()));
            str.append(String.format("Trip source station- %s\nTrip destination station - %s\n", trip.getKey().getSourceStation(), trip.getKey().getDestinationStation()));
            if (trip.getKey().isRequestByStartTime()) {
                str.append(String.format("Trip starting hour- %s\n", trip.getKey().getStartTime().toString()));
                if (trip.getKey().getIsStartTime()) {
                    str.append(String.format("Trip starting hour- %s\n", trip.getKey().getStartTime().toString()));
                } else {
                    str.append(String.format("Trip arrival hour- %s\n", trip.getKey().getArrivalTime().toString()));
                }

                if (trip.getKey().isMatched()) {
                    str.append("This request is already match to suggested trip, here are the details of the trip: \n");
                    str.append(String.format("Trip Match ID- %d\n", trip.getKey().getMatchTrip().getSuggestID()));
                    str.append(String.format("Trip Match owner name- %s\n", trip.getKey().getMatchTrip().getTripOwnerName()));
                    str.append(String.format("Trip Match price- %d\n", trip.getKey().getMatchTrip().getTripPrice()));
                    str.append(String.format("Required fuel for request- %d\n", calcRequiredFuelToRequest(trip.getKey())));
                    if (trip.getKey().isRequestByStartTime()) {
                        str.append(String.format("Starting hour- %s\n\n", trip.getKey().getStartTime()));
                    } else {
                        str.append(String.format("Arrival hour- %s\n\n", trip.getKey().getStartTime()));
                    }
                } else {
                    str.append("This trip request isn't matched yet\n\n");
                }
            }
        }
        return str.toString();
    }

    public String getAllNotMatchedRequestsTrip() {
        StringBuilder str = new StringBuilder();

        if (tripRequestUtil.getAllRequestTrips().size() > 0) {
            str.append("Here are all the requests trip that didn't match yet\n");
        } else {
            str.append("There are no requests trip that didn't match yet\n");
            return str.toString();
        }

        for (Map.Entry<TripRequest, Integer> trip : tripRequestUtil.getAllRequestTrips().entrySet()) {
            if (!trip.getKey().isMatched()) {
                str.append(String.format("Request ID- %d\n", trip.getKey().getRequestID()));
                str.append(String.format("Name of requester- %s\n", trip.getKey().getNameOfOwner()));
                str.append(String.format("Source stations- %s\n", trip.getKey().getSourceStation()));
                str.append(String.format("Destination stations- %s\n", trip.getKey().getDestinationStation()));
                if (trip.getKey().isRequestByStartTime()) {
                    str.append(String.format("Starting hour- %s\n\n", trip.getKey().getStartTime()));
                } else {
                    str.append(String.format("Arrival hour- %s\n\n", trip.getKey().getStartTime()));
                }
            }
        }
        return str.toString();
    }

    private int calcRequiredFuelToRequest(TripRequest tripRequest) {
        return calcRequiredFuelToRequest(tripRequest.getMatchTrip(), tripRequest);
    }

    private String findRouteToRequest(TripSuggest tripSuggest, TripRequest tripRequest) {
        String[] stations = tripSuggest.getTripRoute().split(",");
        boolean start = false;
        String sourceStation = tripRequest.getSourceStation();
        String destinationStation = tripRequest.getDestinationStation();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < stations.length; i++) {
            if (!start) {
                if (stations[i].equals(sourceStation)) {
                    start = true;
                    str.append(stations[i]);
                    str.append(",");
                }
            } else {
                if (stations[i].equals(destinationStation)) {
                    str.append(stations[i]);
                    break;
                } else {
                    str.append(stations[i]);
                    str.append(",");
                }
            }
        }

        return str.toString();
    }

    private Integer getRequestTripID(TripRequest trip) {
        return tripRequestUtil.getTripID(trip);
    }

    public TripRequest addNewTripRequest(String[] inputs) {
        TripRequest newRequest = null;
        int hour = Integer.parseInt(inputs[3].split(":")[0]);
        int minutes = Integer.parseInt(inputs[3].split(":")[1]);
        int day = Integer.parseInt(inputs[5]);
        newRequest = new TripRequest(inputs[0], inputs[1], inputs[2], minutes, hour, day, inputs[4].equals("S"));
        tripRequestUtil.addRequestTrip(newRequest);
        return newRequest;
    }

    public TripSuggest addNewTripSuggest(String[] inputs) {
        int hour = Integer.parseInt(inputs[3].split(":")[0]);
        int minutes = Integer.parseInt(inputs[3].split(":")[1]);
        Route newTripSuggestRoute = new Route();
        String stringPath = setInputPathToSystemStylePath(inputs[1]);
        newTripSuggestRoute.setPath(stringPath);
        int day = 0;
        int ppk = 0;
        int scheduleTypeInt = 0;
        int tripCapacity = 0;

        try {
            day = Integer.parseInt(inputs[2]);
            scheduleTypeInt = Integer.parseInt(inputs[4]);
            ppk = Integer.parseInt(inputs[5]);
            tripCapacity = Integer.parseInt(inputs[6]);
        } catch (NumberFormatException e) {
            addErrorMessageToMenuOrder("NumberFormatException occur ");
        }
        TripSuggest tripSuggest = new TripSuggest(inputs[0], newTripSuggestRoute, minutes, hour, day, scheduleTypeInt, ppk, tripCapacity);

        tripSuggestUtil.addSuggestTrip(tripSuggest);

        return tripSuggest;
    }

    public String getRequestValidationSuccessMessage() {
        return tripRequestUtil.getValidationSuccessMessage();
    }

    public TransPool getTransPool() {
        return transPool;
    }

    public int getLengthBetweenStations(String pathFrom, String pathTo) {
        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            if (path.getFrom().equals(pathFrom) && path.getTo().equals(pathTo)) {
                return path.getLength();
            }
        }
        return -1;
    }

    public int getRequiredFuelToPath(String pathFrom, String pathTo) {
        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            if (path.getFrom().equals(pathFrom) && path.getTo().equals(pathTo)) {
                return path.getLength() / path.getFuelConsumption();
            }
        }
        return -1;
    }

    public boolean validateRequestIDIsExist(String input) {
        Integer requestID = Integer.parseInt(input);
        return tripRequestUtil.isRequestIDExist(requestID);
    }

    public List<RoadTrip> getPotentialRoadTripsToRequest(String input) {
        matchingUtil = new MatchingUtil();
        return matchingUtil.findRoadTripsMatchToRequestTrip(input);
    }

    public double calcMinutesToRoute(String pathFrom, String pathTo) {
        boolean isPathOneWay;
        double retVal = 0;
        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            if (path.getFrom().equals(pathFrom) && path.getTo().equals(pathTo)) {
                return (double) path.getLength() / path.getSpeedLimit();
            } else {
                try {
                    isPathOneWay = path.isOneWay();
                } catch (NullPointerException e) {
                    isPathOneWay = false;
                }
                if (!isPathOneWay) {
                    if (path.getTo().equals(pathFrom) && path.getFrom().equals(pathTo)) {
                        return (double) path.getLength() / path.getSpeedLimit();
                    }
                }
            }
        }
        return 0;
    }

    public String convertPotentialSuggestedTripsToString(List<RoadTrip> potentialSuggestedTrips, String requestID) {
        TripRequest tripRequest = getTripRequestByID(Integer.parseInt(requestID));
        StringBuilder str = new StringBuilder();
        int index = 1;


        if (potentialSuggestedTrips.size() == 0) {
            str.append("The system couldn't found suggested trips to you trip request, sorry.\n");
            return str.toString();
        } else {
            str.append("\nPotential suggested trips:\n");
            for (RoadTrip trip : potentialSuggestedTrips) {
                str.append(String.format("%d- \n", index));
                str.append(trip.toString());
                index++;
            }
        }

        return str.toString();
    }

    private int calcRequiredFuelToRequest(TripSuggest tripsuggest, TripRequest tripRequest) {
        String route = findRouteToRequest(tripsuggest, tripRequest);
        return TripSuggestUtil.calcRequiredFuel(route);
    }

    public TripRequest getTripRequestByID(int requestID) {
        return tripRequestUtil.getTripRequestByID(requestID);
    }

    public Map<TripSuggest, Integer> getAllSuggestedTripsMap() {
        return tripSuggestUtil.getAllSuggestedTrips();
    }

    public boolean validateMenuInput(String input) {
        return validator.validateMenuInput(input);
    }

    public StringBuilder getMenuErrorMessage() {
        return validator.getMenuErrorMessage();
    }

    public void setNullableMenuErrorMessage() {
        validator.setNullableMenuErrorMessage();
    }

    public String matchTripRequest(String input, List<RoadTrip> potentialRoadTrips, String
            requestIDAndAmountToMatch) {
        String[] inputs = requestIDAndAmountToMatch.split(",");
        int requestID = Integer.parseInt(inputs[0]);
        int roadTripIndex = Integer.parseInt(input);
        RoadTrip roadTrip = null;

        for (int i = 0; i < potentialRoadTrips.size(); i++) {
            if (potentialRoadTrips.get(i).getIndex() == roadTripIndex) {
                roadTrip = potentialRoadTrips.get(i);
                break;
            }
        }
        TripRequest tripRequest = tripRequestUtil.getTripRequestByID(requestID);
        matches.put(tripRequest, roadTrip);
        return SUCCESS_MATCHING;
    }

    public void addErrorMessageToMenuOrder(String errorMessage) {
        if (this.menuOrderErrorMessage == null) {
            this.menuOrderErrorMessage = new LinkedList<>();
        }
        menuOrderErrorMessage.add(errorMessage);
    }


    public String setInputPathToSystemStylePath(String dotPath) {
        return dotPath.replace('-', ',');
    }

//---------------------------- RequestValidator Section ----------------------------

    public boolean validateTripRequestInput(String[] inputs) {
        return validator.validateTripRequestInput(inputs);
    }

    public String getRequestValidationErrorMessage() {
        return validator.getAddNewTripRequestErrorMessage();
    }

    public List<String> validateChooseRequestAndAmountOfSuggestedTripsInput(String input) {
        return validator.validateChooseRequestAndAmountOfSuggestedTripsInput(input);
    }

    public String getChooseRequestAndAmountOfSuggestedTripsErrorMessage() {
        return validator.getChooseRequestAndAmountOfSuggestedTripsErrorMessage();
    }

    public void deleteChooseRequestAndAmountErrorMessage() {
        validator.deleteChooseRequestAndAmountErrorMessage();
    }

    public void deleteNewTripRequestErrorMessage() {
        validator.deleteErrorMessageOfAddNewTripRequest();
    }

    public void deleteNewTripSuggestErrorMessage() {
        validator.deleteErrorMessageOfAddNewTripSuggest();
    }

    public String getChoosePotentialTripInputErrorMessage() {
        return validator.getChoosePotentialTripInputErrorMessage();
    }

    public boolean validateChoosePotentialTripInput(String input, List<RoadTrip> potentialSuggestedTrips) {
        return validator.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
    }

    //---------------------------- SuggestValidator Section ----------------------------
    public boolean validateTripSuggestInput(String[]
                                                    inputTripSuggestString, HashSet<String> allStationsLogicNames) {
        return validator.validateTripSuggestInput(inputTripSuggestString, allStationsLogicNames);
    }

    public String getSuggestValidationErrorMessage() {
        return validator.getAddNewTripSuggestErrorMessage();
    }

    public String getSuggestValidationSuccessMessage() {
        return validator.getSuggestValidationSuccessMessage();
    }

    public void deleteSuggestTripValidationErrorMessage() {
        validator.deleteSuggestTripErrorMessage();
    }

    public String getXMLValidationsSuccessMessage() {
        return XMLValidationsImpl.getValidXmlMessage();
    }

    public static String convertDoubleTimeToStrTime(double time) {
        String[] vals = String.valueOf(time).split("\\.");
        if (vals[1].length() == 1) {
            vals[1] += "0";
        }
        return vals[0] + ":" + vals[1];
    }

    public void moveTimeForward() {
        TimeManager.getInstance().moveTimeForward(1);
    }

    public void moveTimeBack() {
        TimeManager.getInstance().moveTimeBack(1);
    }

    public Map<TripSuggest, String> getCurrentTripsSuggestAndStation() {
        Map<TripSuggest, String> mapToRet = new HashMap<>();
        Map<TripSuggest, Integer> suggestedTrips = tripSuggestUtil.getAllSuggestedTrips();
        TripSuggest currTrip;

        for (Map.Entry<TripSuggest, Integer> entry : suggestedTrips.entrySet()) {
            currTrip = entry.getKey();
            if (checkIfTripActiveNow(currTrip)) {
                String currStation = findTripCurrentStation(currTrip);
                mapToRet.put(currTrip, currStation);
            }
        }
        return null;
    }


    public static String findTripCurrentStation(TripSuggest trip) {
        Time timeSystem = TimeManager.getInstance().getCurrTime();
        int hourSystem = timeSystem.getHours();
        int minutesSystem = timeSystem.getMinutes();

        String prevStation = trip.getTripRoute().split(",")[0];
        String[] tripRoute = trip.getTripRoute().split(",");
        Time currTripTime;
        int currTripHour;
        int currTripMinutes;

        for (String station : tripRoute) {
            currTripTime = trip.getArrivalHourToSpecificStation(station);
            currTripHour = currTripTime.getHours();
            currTripMinutes = currTripTime.getMinutes();

            if (currTripHour > hourSystem) {
                return prevStation;
            } else if (currTripHour == hourSystem) {
                if (currTripMinutes > minutesSystem) {
                    return prevStation;
                } else if (currTripMinutes == minutesSystem) {
                    return station;
                }
            } else {
                return prevStation;
            }
            prevStation = station;
        }

        return null;
    }


    private boolean checkIfTripActiveNow(TripSuggest currTrip) {
        Time time = TimeManager.getInstance().getCurrTime();
        int day = time.getDay();
        int hours = time.getHours();
        int minutes = time.getMinutes();

        if (checkIfTripActivateOnDAy(currTrip, day)) {
            if (checkTripOnHour(currTrip, hours, minutes)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTripOnHour(TripSuggest currTrip, int hoursSystem, int minutesSystem) {
        Time startingTime = currTrip.getStartingTime();
        int startingHour = startingTime.getHours();
        int startingMinutes = startingTime.getMinutes();
        Time arrivalTime = currTrip.getArrivalTime();
        int arrivalHour = arrivalTime.getHours();
        int arrivalMinutes = arrivalTime.getMinutes();

        if (arrivalHour < hoursSystem) {
            return false;
        } else if (arrivalHour == hoursSystem) {
            if (arrivalMinutes < minutesSystem) {
                return false;
            } else {
                return true;
            }
        } else {
            if (startingHour < hoursSystem) {
                return true;
            } else if (startingHour == hoursSystem) {
                if (startingMinutes < minutesSystem) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private boolean checkIfTripActivateOnDAy(TripSuggest trip, int currentDaySystem) {
        int tripDay = trip.getStartingDay();
        TripSuggest.RecurrencesTypes recurrences = trip.getRecurrencesType();

        if (recurrences.equals(TripSuggest.RecurrencesTypes.ONE_TIME_ONLY)) {
            return trip.getStartingDay() == currentDaySystem;
        } else {
            while (tripDay <= currentDaySystem) {
                if (tripDay == currentDaySystem) {
                    return true;
                } else {
                    tripDay += trip.getRecurrencesType().getValue();
                }
            }
            return false;
        }
    }

    public List<String> getAllPlannedTripsOwnerNames() {
        return suggestTripOwners;
    }
}


