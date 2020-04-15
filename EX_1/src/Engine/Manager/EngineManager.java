package Engine.Manager;

import Engine.InputValidations.Validator;
import Engine.MatchingUtil.MatchingUtil;
import Engine.TripRequests.TripRequest;
import Engine.TripRequests.TripRequestsUtil;
import Engine.TripSuggestUtil.TripSuggest;
import Engine.TripSuggestUtil.TripSuggestUtil;
import Engine.XMLValidations.XMLValidationsImpl;
import Engine.XMLLoading.jaxb.schema.SchemaBasedJAXBMain;
import Engine.XMLLoading.jaxb.schema.generated.Path;
import Engine.XMLLoading.jaxb.schema.generated.Stop;
import Engine.XMLLoading.jaxb.schema.generated.TransPool;
import java.util.List;
import java.util.Map;

public class EngineManager {
    private static EngineManager engineManagerInstance;
    private TransPool transPool;
    private static TripRequestsUtil tripRequestUtil;
    private static TripSuggestUtil tripSuggestUtil;
    private static MatchingUtil matchingUtil;
    private static Validator validator;
    private static boolean isXMLFileLoaded;
    private String menuOrderErrorMessage;

    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManagerInstance == null) {
            engineManagerInstance = new EngineManager();
            tripSuggestUtil = new TripSuggestUtil();
            tripRequestUtil = new TripRequestsUtil();
            matchingUtil = new MatchingUtil();
            validator = new Validator();
            isXMLFileLoaded = false;
        }
        return engineManagerInstance;
    }

    public String LoadXML(String myPathToTheXMLFile, List<String> errors) {
        SchemaBasedJAXBMain jax = new SchemaBasedJAXBMain();
        transPool = jax.init();

        XMLValidationsImpl xmlValidator = new XMLValidationsImpl(transPool);
//        if (xmlValidator.validateXmlFile(myPathToTheXMLFile, errors)) {
            tripSuggestUtil.convertPlannedTripsToSuggestedTrips(transPool.getPlannedTrips().getTransPoolTrip());
            isXMLFileLoaded = true;
            return xmlValidator.getValidMessage();
////        } else {
//           return xmlValidator.getErrorMessage();
//        }
    }

    public String getAllStationsName () {
        StringBuilder str = new StringBuilder();
        str.append("All stations names: \n");
        int index = 1;
        for(Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            str.append(String.format("%d - %s\n",index, stop.getName()));
            index++;
        }
        return str.toString();
    }

    public String getAllSuggestedTrips () {
        StringBuilder str = new StringBuilder();
        if (tripSuggestUtil.getAllSuggestedTrips().size() > 0) {
            str.append("All suggested trips: \n");
        }
        else {
            str.append("There are no suggested trips");
            return str.toString();
        }

        int index = 1;
        for(Map.Entry<TripSuggest, Integer> trip : tripSuggestUtil.getAllSuggestedTrips().entrySet()) {
            str.append(String.format("%d - \n", index));
            index++;
            str.append(String.format("Trip ID - %d\n", getSuggestTripID(trip.getKey())));
            str.append(String.format("Trip Owner - %s\n", trip.getKey().getTripOwnerName()));
            str.append(String.format("Trip Route - %s\n", trip.getKey().getTripRoute()));
            str.append(String.format("Trip Price - %s\n", trip.getKey().getTripPrice()));
            str.append(String.format("Trip starting hour - %s\nTrip arrival hour - %s\n", trip.getKey().getStartingHour(), trip.getKey().getArrivalHour()));
            str.append(String.format("Trip available sits - %s\n", trip.getKey().getRemainingCapacity()));
            str.append(String.format("Exists passengers trip - %s\n", getListOfAllTripPassengersID(trip.getKey())));
            str.append(String.format("Required fuel to trip - %s.L\n", trip.getKey().getRequiredFuel()));
            str.append(String.format("￿Stations to stop -  - %s\n", trip.getKey().getStationsDetailsAsString()));
        }
        return str.toString();
    }

    private String getListOfAllTripPassengersID(TripSuggest trip) {
        StringBuilder str = new StringBuilder();
        int index = 1;

        for(Integer id : trip.getPassengers()) {
            str.append(String.format("%d: Passenger ID - %d",index, id));
            index++;
        }

        return str.toString();
    }

    private Integer getSuggestTripID(TripSuggest trip) {
        return tripSuggestUtil.getTripID(trip);
    }

    public String getAllTripRequests () {
        StringBuilder str = new StringBuilder();
        if(tripRequestUtil.getAllRequestTrips().size() > 0) {
            str.append("All requested trips:\n");
        }
        else {
            str.append("There are no trip requests\n");
            return str.toString();
        }

        for(Map.Entry<TripRequest, Integer> trip : tripRequestUtil.getAllRequestTrips().entrySet()) {
            str.append(String.format("Trip ID - %d\n", getRequestTripID(trip.getKey())));
            str.append(String.format("Trip requester - %s\n", trip.getKey().getNameOfOwner()));
            str.append(String.format("Trip source station - %s\nTrip destination station - %s\n", trip.getKey().getSourceStation(), trip.getKey().getDestinationStation()));
            str.append(String.format("Trip starting hour - %d\n", trip.getKey().getStartingHour()));

            if(trip.getKey().isMatched()) {
                str.append("This request is already match to suggested trip, here are the details of the trip: \n");
                str.append(String.format("Trip Match ID - %d\n", trip.getKey().getMatchTrip().getSuggestID()));
                str.append(String.format("Trip Match owner name - %s\n", trip.getKey().getMatchTrip().getTripOwnerName()));
                str.append(String.format("Trip Match price - %d\n", trip.getKey().getMatchTrip().getTripPrice()));
                str.append(String.format("Trip Match estimate arrival hour - %d\n", trip.getKey().getMatchTrip().getArrivalHour()));
                str.append(String.format("Required fuel for request - %d\n", calcRequiredFuelToRequest(trip.getKey())));
            }
            else {
                str.append("This trip request isn't matched yet\n\n");
            }
        }
        return str.toString();
    }

    //have to fix
    private int calcRequiredFuelToRequest(TripRequest tripRequest) {
        return 1;
    }

    private Integer getRequestTripID(TripRequest trip) {
        return tripRequestUtil.getTripID(trip);
    }

    public void addNewTripRequest(String input) {
        String[] inputs = input.split(",");
        TripRequest newRequest = new TripRequest(inputs[0], inputs[1], inputs[2], 10); // have to fix startingHour (parse to int somehow)
        tripRequestUtil.addRequestTrip(newRequest);
    }

    public boolean validateTripRequestInput(String input) {
        return validator.validateTripRequestInput(input);
    }

    public String getRequestValidationErrorMessage () {
        return validator.getAddNewTripRequestErrorMessage();
    }

    public String getRequestValidationSuccessMessage () {
        return tripRequestUtil.getValidationSuccessMessage();
    }

    public TransPool getTransPool() {
        return transPool;
    }

    public int getLengthBetweenStations(String pathFrom, String pathTo) {
        for(Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            if(path.getFrom().equals(pathFrom) && path.getTo().equals(pathTo)) {
                return path.getLength();
            }
        }
        return -1;
    }

    public int getRequiredFuelToPath(String pathFrom, String pathTo) {
        for(Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            if(path.getFrom().equals(pathFrom) && path.getTo().equals(pathTo)) {
                return path.getLength()/path.getFuelConsumption();
            }
        }
        return -1;
    }

    public int calcArrivalHourToRoute(String pathFrom, String pathTo, int startingHour) {
        int arrivalHour = startingHour;
        for(Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            if(path.getFrom().equals(pathFrom) && path.getTo().equals(pathTo)) {
                arrivalHour += path.getLength()/path.getSpeedLimit();
            }
        }
        return arrivalHour;

    }

    public String getAllNotMatchedRequestsTrip() {
        StringBuilder str = new StringBuilder();

        if(tripRequestUtil.getAllRequestTrips().size() > 0) {
            str.append("Here are all the requests trip that didn't match yet\n");
        }
        else {
            str.append("There are no requests trip that didn't match yet\n");
            return str.toString();
        }

        for(Map.Entry<TripRequest, Integer> trip : tripRequestUtil.getAllRequestTrips().entrySet()) {
            if(!trip.getKey().isMatched()) {
                str.append(String.format("Request ID - %d\n", trip.getKey().getRequestID()));
                str.append(String.format("Name of requester - %s\n", trip.getKey().getNameOfOwner()));
                str.append(String.format("Source stations - %s\n", trip.getKey().getSourceStation()));
                str.append(String.format("Destination stations - %s\n", trip.getKey().getDestinationStation()));
                str.append(String.format("Starting hour - %s\n\n", trip.getKey().getStartingHour()));
            }
        }
        return str.toString();
    }

    public boolean validateChooseRequestAndAmountOfSuggestedTripsInput(String input) {
       return validator.validateChooseRequestAndAmountOfSuggestedTripsInput(input);
    }

    public boolean validateRequestIDIsExist(String input) {
        Integer requestID = Integer.parseInt(input);
        return tripRequestUtil.isRequestIDExist(requestID);
    }

    public String getChooseRequestAndAmountOfSuggestedTripsErrorMessage() {
        return validator.getChooseRequestAndAmountOfSuggestedTripsErrorMessage();
    }

    public void deleteNewTripRequestErrorMessage() {
        validator.deleteErrorMessageOfAddNewTripRequest();
    }

    public TripSuggest[] findPotentialMatchToRequestTrip(String input) {
        TripSuggest[] suggestedTrips = matchingUtil.findPotentialMatchToRequestTrip(input);
        for(int i = 0; i < suggestedTrips.length; i++) {
            if(suggestedTrips[i] != null) {
                return suggestedTrips;
            }
        }
        return null;
    }

    public String convertPotentialSuggestedTripsToString(TripSuggest[] potentialSuggestedTrips) {
        StringBuilder str = new StringBuilder();
        str.append("\nPotential suggested trips:\n");
        int index = 1;

        if(potentialSuggestedTrips.length == 0) {
            str.append("The system couldn't found suggested trips to you trip request, sorry.\n");
            return str.toString();
        }

        for(TripSuggest trip : potentialSuggestedTrips) {
            str.append(String.format("Trip ID - %d\n", trip.getSuggestID()));
            str.append(String.format("Trip owner name - %s\n", trip.getTripOwnerName()));
            str.append(String.format("Trip price - %d\n", trip.getTripPrice()));
            str.append(String.format("Trip estimate time to arrival - %d\n", trip.getArrivalHour()));
            str.append(String.format("Required fuel to your trip - %d\n\n", null));//have to fix
            index++;
        }
        return str.toString();
    }

    public TripRequest getTripRequestByID(int requestID) {
        return tripRequestUtil.getTripRequestByID(requestID);
    }

    public Map<TripSuggest, Integer> getAllSuggestedTripsMap() {
        return tripSuggestUtil.getAllSuggestedTrips();
    }

    public String getChoosePotentialTripInputErrorMessage() {
        return validator.getChoosePotentialTripInputErrorMessage();
    }

    public boolean validateChoosePotentialTripInput(String input, TripSuggest[] potentialSuggestedTrips) {
        return validator.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
    }

    public boolean validateMenuInput(String input) {
        if(validateMenuOrder(input)) {
            return validator.validateMenuInput(input);
        }
        else {
            menuOrderErrorMessage = "XML file didn't load yet, please load the file and try again.\n";
            return false;
        }

    }

    private boolean validateMenuOrder(String input) {
        return true;
    }

    public String getMenuErrorMessage() {
        return validator.getMenuErrorMessage();
    }

    public void deleteChooseRequestAndAmountErrorMessage () {
        validator.deleteChooseRequestAndAmountErrorMessage();
    }

    public String matchRequestToSuggest(String input, TripSuggest[] potentialSuggestedTrips, String requestIDAndAmountToMatch) {
        String[] inputs = requestIDAndAmountToMatch.split(",");
        int requestID = Integer.parseInt(inputs[0]);
        int suggestedTripID = Integer.parseInt(input);
        TripSuggest tripSuggest = null;
        for(int i = 0; i < potentialSuggestedTrips.length; i++) {
            if(potentialSuggestedTrips[i].getSuggestID() == suggestedTripID) {
                tripSuggest = potentialSuggestedTrips[i];
                break;
            }
        }
        TripRequest tripRequest = tripRequestUtil.getTripRequestByID(requestID);
        return matchingUtil.matchRequestToSuggest(tripSuggest, tripRequest);
    }
}

