package Manager;

import GraphBuilder.GraphBuilderUtil;
import MatchingUtil.MatchUtil;
import MatchingUtil.RoadTrip;
import MatchingUtil.Station;
import MatchingUtil.StationsUtil;
import MatchingUtil.SubTrip;
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
import com.fxgraph.graph.Graph;


import java.io.FileNotFoundException;
import java.util.*;

public class EngineManager {
    private static TimeManager timeManager;
    private static EngineManager engineManagerInstance;
    private static TransPool transPool;
    private static TripRequestsUtil tripRequestUtil;
    private static TripSuggestUtil tripSuggestUtil;
    private static MatchUtil matchUtil;
    private static Validator validator;
    private static List<String> suggestTripOwners;
    private static Map<TripRequest, RoadTrip> matches;
    private static int requestIDCache;
    private static StationsUtil stationsUtil;

    private List<String> menuOrderErrorMessage;
    private static List<RoadTrip> potentialCacheList;
    private static GraphBuilderUtil graphBuilderUtil;
    private static final String SUCCESS_MATCHING = "Your trip request was match to trip suggested successfully\n";

    public TripSuggestUtil getTripSuggestUtil() {
        return tripSuggestUtil;
    }

    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManagerInstance == null) {
            engineManagerInstance = new EngineManager();
            tripSuggestUtil = new TripSuggestUtil();
            tripRequestUtil = new TripRequestsUtil();
            validator = Validator.getInstance();
            matches = new HashMap<>();
            timeManager = TimeManager.getInstance();
            matchUtil = new MatchUtil();
            potentialCacheList = new LinkedList<>();
            suggestTripOwners = new ArrayList<>();
            stationsUtil = new StationsUtil();
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
                graphBuilderUtil = new GraphBuilderUtil(transPool);
                cleanEngine();
                tripSuggestUtil.convertPlannedTripsToSuggestedTrips(transPool.getPlannedTrips().getTransPoolTrip());
                findAllPlannedTripsOwnerNames();
            } catch (NullPointerException e) {
                suggestTripOwners = new ArrayList<>();
            }
        }
        return errors;
    }

    private void cleanEngine() {
        tripSuggestUtil.getAllSuggestedTrips().clear();
        tripSuggestUtil.restSuggestedTripsId();
        tripRequestUtil.getAllRequestTrips().clear();
        timeManager.setCurrentTime(new Time(0, 0, 1));
        suggestTripOwners.clear();
    }

    private void findAllPlannedTripsOwnerNames() {
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

    public HashSet<String> getAllLogicStationsName() {
        HashSet<String> hashSet = new HashSet<>();
        List<Stop> stops = transPool.getMapDescriptor().getStops().getStop();
        for (Stop stop : stops) {
            hashSet.add(stop.getName());
        }
        return hashSet;
    }

    private String findRouteToRequest(TripSuggest tripSuggest, TripRequest tripRequest) {
        Station[] stations = tripSuggest.getTripStations();
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

    public static GraphBuilderUtil getGraphBuilderUtil() {
        return graphBuilderUtil;
    }

    public static void setGraphBuilderUtil(GraphBuilderUtil graphBuilderUtil) {
        EngineManager.graphBuilderUtil = graphBuilderUtil;
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

    public void addNewTripRequest(TripRequest request) {
        tripRequestUtil.addRequestTrip(request);
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

    public void addNewTripSuggest(TripSuggest tripSuggest) {
        tripSuggestUtil.addSuggestTrip(tripSuggest);
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

    public List<String> validateRequestIDExistInMatchedRequestTrip(String input) {
        List<String> errors = new ArrayList<>();
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (Exception ex) {
            errors.add("Your choice wasn't a number\n");
            return errors;
        }
        if (tripRequestUtil.isRequestIDExistInMatchedRequestTrips(id)) {
            return errors;
        } else {
            errors.add("Trip request ID isn't exist in the previous list.\n");
            return errors;
        }
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

    public Map<Integer, TripSuggest> getAllSuggestedTripsMap() {
        return tripSuggestUtil.getAllSuggestedTrips();
    }

    public String matchTripRequest(String input, String requestIDAndAmountToMatch) {
        if (!validaRoadTripChoice(input)) {
            return null;
        }
        String[] inputs = requestIDAndAmountToMatch.split(",");
        int requestID = Integer.parseInt(inputs[0]);
        int roadTripIndex = Integer.parseInt(input);
        RoadTrip roadTrip = null;

        for (int i = 0; i < potentialCacheList.size(); i++) {
            if (i + 1 == roadTripIndex) {
                roadTrip = potentialCacheList.get(i);
                break;
            }
        }
        TripRequest tripRequest = tripRequestUtil.getTripRequestByID(requestID);
        matches.put(tripRequest, roadTrip);
        tripRequest.setMatched(true);
        if(tripRequest.isRequestByStartTime()) {
            tripRequest.setArrivalTime(roadTrip.getArrivalTime());
        }
        else {
            tripRequest.setStartTime(roadTrip.getStartTime());
        }

        tripRequest.setMatchTrip(roadTrip);
        updateSuggestsCapacityPerTimeMap(roadTrip);
        return roadTrip.getRoadStory();
    }

    private void updateSuggestsCapacityPerTimeMap(RoadTrip roadTrip) {
        LinkedList<Station> stations;
        Station[] stationsArr;
        int totalMinutes;
        for(SubTrip subTrip : roadTrip.getSubTrips()) {
            stations = subTrip.getRoute();
            stationsArr = new Station[stations.size() - 1];
            copyLinkedListToArr(stations, stationsArr);
            for(int i =0; i < stationsArr.length; i++) {
                totalMinutes = calcTotalMinutes(stationsArr[i].getDay(), stationsArr[i].getHour(), stationsArr[i].getMinutes());
                subTrip.getTrip().addNewItemToCapacityPerTimeMap(totalMinutes);
            }
        }

    }

    private int calcTotalMinutes(int day, int hour, int minutes) {
        return (day * 24 * 60) + (hour * 60) + (minutes);
    }

    private void copyLinkedListToArr(LinkedList<Station> stations, Station[] stationsArr) {
        int index = 0;
        for(Station station : stations) {
            if(stations.getLast().getName().equals(station.getName())) {
                break;
            }
            stationsArr[index] = station;
            index++;
        }
    }

    private boolean validaRoadTripChoice(String inputStr) {
        int input = 0;

        try {
            input = Integer.parseInt(inputStr);
        } catch (Exception ex) {
            return false;
        }
        if (input < 1) {
            return false;
        } else if (potentialCacheList.size() < input) {
            return false;
        }
        return true;
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

    public void moveTimeForward(int choose) {
        timeManager.moveTimeForward(choose);
        updateSuggestedTrips();
    }

    private void updateSuggestedTrips() {
        tripSuggestUtil.updateSuggestedTrips();
    }

    public void moveTimeBack(int choose) throws Exception {
        timeManager.moveTimeBack(choose);
        updateSuggestedTrips();
    }

    public List<String> getListDetailsPerTime() {
        //String of name, id, path, current station
        List<String> retList = new ArrayList<>();
        Map<Integer, TripSuggest> suggestedTrips = tripSuggestUtil.getAllSuggestedTrips();

        for (Map.Entry<Integer, TripSuggest> entry : suggestedTrips.entrySet()) {
            if (entry.getValue().isActive()) {
                Station currStation = findTripCurrentStation(entry.getValue());

                String roadTrip = tripSuggestUtil.getStringRoadTripOfSuggestedTrip(entry.getKey().toString());
                retList.add(String.format("ID: %s, Owner name: %s, Route: %s, Curr Stat: %s",
                        entry.getValue().getSuggestID(), entry.getValue().getTripOwnerName(),
                        roadTrip, currStation.getName()));
            }
        }
        return retList;
    }

    public Time getCurrentSystemTime() {
        return timeManager.getCurrTime();
    }

    public static Station findTripCurrentStation(TripSuggest trip) {
        Time timeSystem = timeManager.getCurrTime();
        int hourSystem = timeSystem.getHours();
        int minutesSystem = timeSystem.getMinutes();

        Time currTripTime;
        int currTripHour;
        int currTripMinutes;
        Station prevStation = trip.getFirstStation();

        for (Station station : trip.getTripStations()) {
            currTripTime = trip.getArrivalTimeToStation(station);
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

    public List<String> getAllPlannedTripsOwnerNames() {
        return suggestTripOwners;
    }

    public List<String> findPotentialSuggestedTripsToMatch(String inputMatchingString) {
        String[] elements = inputMatchingString.split(",");
        String tripRequestID = elements[0];
        String amountS = elements[1];
        TripRequest request = getTripRequestByID(Integer.parseInt(tripRequestID));
        int amount = Integer.parseInt(amountS);
        LinkedList<LinkedList<SubTrip>> potentialRoadTrips = matchUtil.makeAMatch(request, amount);
        updateSubTripsValues(potentialRoadTrips);
        potentialCacheList = convertTwoLinkedListToOneRoadTripLinkedList(potentialRoadTrips, request);

        int requestID = Integer.parseInt(inputMatchingString.split(",")[0]);
        return convertToStr(potentialCacheList, tripRequestUtil.getTripRequestByID(requestID));
    }

    private void updateSubTripsValues(LinkedList<LinkedList<SubTrip>> potentialRoadTrips) {
        for(LinkedList<SubTrip> subTrips : potentialRoadTrips) {
            for(SubTrip subTrip : subTrips) {
                subTrip.calcCost();
                subTrip.calcRequiredFuel();
                subTrip.buildSubTripStory();
                subTrip.calcStartArrivalTime();
            }
        }
    }

    private List<RoadTrip> convertTwoLinkedListToOneRoadTripLinkedList(LinkedList<LinkedList<SubTrip>> potentialRoadTrips, TripRequest request) {
        LinkedList<RoadTrip> roadTrips = new LinkedList<RoadTrip>();
        for(LinkedList<SubTrip> roadTrip : potentialRoadTrips) {
            roadTrips.add(createRoadTripFromLinkListSubTrips(roadTrip, request));
        }

        return roadTrips;
    }

    private RoadTrip createRoadTripFromLinkListSubTrips(LinkedList<SubTrip> subRoadTrips, TripRequest request) {
        RoadTrip roadTrip = new RoadTrip();
        roadTrip.setTripRequest(request);
        for(SubTrip subTrip : subRoadTrips) {
            roadTrip.addSubTripToRoadTrip(subTrip);
        }
        roadTrip.calcRequiredFuel();
        roadTrip.calcTotalCost();
        roadTrip.calcStartArrivalTime();
        roadTrip.buildRoadTripStory();

        return roadTrip;
    }

    private List<String> convertToStr(List<RoadTrip> potentialCacheList, TripRequest tripRequest) {
        List<String> potentialRoadTripsStr = new ArrayList<>();
        int index = 0;
        for (RoadTrip roadTrip : potentialCacheList) {
            index++;
            if (tripRequest.isRequestByStartTime()) {
                potentialRoadTripsStr.add(String.format("Index %d:\n%s \n", index, roadTrip.getRoadStory()));
            } else {
                potentialRoadTripsStr.add(String.format("Index %d:\n%s \n", index, roadTrip.getRoadStory()));
            }
        }
        return potentialRoadTripsStr;
    }

    public List<String> getAllMatchedTripRequest() {
        return tripRequestUtil.getAllMatchedTripRequestAsString();
    }

    public List<String> getTripSuggestIdsFromTripRequestWhichNotRankYet(String requestIDstr) {
        int requestID = 0;
        List<String> retVal = new ArrayList<>();
        try {
            requestID = Integer.parseInt(requestIDstr);
        } catch (Exception ex) {
            retVal.add("Your choice wasn't a number.\n");
        }
        TripRequest request = getTripRequestByID(requestID);
        RoadTrip requestRoadTrip = request.getMatchTrip();
        LinkedList<SubTrip> subTrips = requestRoadTrip.getSubTrips();

        for (SubTrip subTrip : subTrips) {
            if(!subTrip.getIsRanked()) {
                retVal.add(String.valueOf(subTrip.getTrip().getSuggestID()));
            }
        }

        if (retVal.size() == 0) {
            retVal.add("You already rated all drivers that part of your road trip");
            return retVal;
        }
        else {
            requestIDCache = requestID;
        }
        return retVal;
    }

    //TripSuggestID, rate, description
    public List<String> validateInputOfRatingDriverOfSuggestIDAndRating(String tripSuggestId, String rateStr, String description) {
        List<String> errors = new ArrayList<>();

        int suggestID = 0;
        int rate;

        try {
            suggestID = Integer.parseInt(tripSuggestId);
        } catch (Exception ex) {
            errors.add("Trip suggest ID isn't a number.\n");
        }
        try {
            rateStr = rateStr.trim();
            rate = Integer.parseInt(rateStr);
        } catch (Exception ex) {
            errors.add("Rating isn't a number.");
            return errors;
        }
        if (tripSuggestUtil.getTripSuggestByID(suggestID) == null) {
            errors.add("Trip suggest isn't exist.\n");
        }
        if (rate < 1 || rate > 5) {
            errors.add("Please insert a number between 1-5 for rating.\n");
            return errors;
        }
        return errors;
    }

    public void rankDriver(String[] inputs) {
        TripSuggest suggest = tripSuggestUtil.getTripSuggestByID(Integer.parseInt(inputs[0]));
        int suggestID = suggest.getSuggestID();
        TripRequest request = getTripRequestByID(requestIDCache);
        RoadTrip roadTrip = request.getMatchTrip();
        LinkedList<SubTrip> subTrips = roadTrip.getSubTrips();
        for(SubTrip subTrip : subTrips) {
            if(subTrip.getTrip().getSuggestID() == suggestID) {
                subTrip.setIsRanked(true);
            }
        }
        if (inputs[2].isEmpty()) {
            suggest.addRatingToDriver(Integer.parseInt(inputs[1]));
        } else {
            suggest.addRatingToDriver(Integer.parseInt(inputs[1]), inputs[2]);
        }
    }

    public Graph getGraph() {
        return graphBuilderUtil.createGraph(getCurrentSystemTime(), transPool);
    }

    public int getXCoorOfStation(String sourceStation) {
       return StationsUtil.getXCoorOfStation(sourceStation, transPool.getMapDescriptor().getStops().getStop());
    }

    public int getYCoorOfStation(String sourceStation) {
        return StationsUtil.getYCoorOfStation(sourceStation, transPool.getMapDescriptor().getStops().getStop());
    }

    public List<TripRequest> getAllMatchedTripRequests() {
        return tripRequestUtil.getAllMatchedTripRequests();
    }

    public List<String> getAllUnmatchedRequests() {
        return tripRequestUtil.getAllUnmatchedRequests();
    }
}
