package Engine.Manager;

import Engine.MatchingUtil.MatchingUtil;
import Engine.TripRequests.TripRequest;
import Engine.TripRequests.TripRequestsUtil;
import Engine.TripSuggestUtil.TripSuggest;
import Engine.TripSuggestUtil.TripSuggestUtil;
import Engine.XMLLoading.XMLValidationsImpl;
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
    private String menuErrorMessage;
    private StringBuilder chooseRequestAndAmountOfSuggestedTripsErrorMessage;

    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManagerInstance == null) {
            engineManagerInstance = new EngineManager();
            tripSuggestUtil = new TripSuggestUtil();
            tripRequestUtil = new TripRequestsUtil();
            matchingUtil = new MatchingUtil();
        }
        return engineManagerInstance;
    }

    public String LoadXML(String myPathToTheXMLFile, List<String> errors) {
        SchemaBasedJAXBMain jax = new SchemaBasedJAXBMain();
        transPool = jax.init();

        XMLValidationsImpl xmlValidator = new XMLValidationsImpl(transPool);
        if (xmlValidator.validateXmlFile(myPathToTheXMLFile, errors)) {
            tripSuggestUtil.convertPlannedTripsToSuggestedTrips(transPool.getPlannedTrips().getTransPoolTrip());
            chooseRequestAndAmountOfSuggestedTripsErrorMessage = new StringBuilder();
            return xmlValidator.getValidMessage();
        } else {
           return xmlValidator.getErrorMessage();
        }
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
            str.append(String.format("Trip starting hour - %s, Trip arrival hour - %s\n", trip.getKey().getStartingHour(), trip.getKey().getArrivalHour()));
            str.append(String.format("Trip available sits - %s\n", trip.getKey().getRemainingCapacity()));
            str.append(String.format("Exists passengers trip - %s\n", getListOfAllTripPassengersID(trip.getKey())));
            str.append(String.format("Trip ID - %s\n", null)); // have to fix
            str.append(String.format("Required fuel to trip - %sL\n", trip.getKey().getRequiredFuel()));
        }
        return str.toString();
    }

    private String getListOfAllTripPassengersID(TripSuggest trip) {
        StringBuilder str = new StringBuilder();
        int index = 1;

        for(Integer id : trip.getPassengers()) {
            str.append(String.format("%d: Passenger - %d",index, id));
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
            str.append(String.format("Trip source station - %s\n Trip destination station - %s\n", trip.getKey().getSourceStation(), trip.getKey().getDestinationStation()));
            str.append(String.format("Trip starting hour - %d\n", trip.getKey().getStartingHour()));

            if(trip.getKey().isMatched()) {
                str.append("This request is already match to suggested trip, here are the details of the trip: \n");
                str.append(String.format("Trip Match ID - %d\n", null)); // have to fix
                str.append(String.format("Trip Match ID - %d\n", null));
                str.append(String.format("Trip Match ID - %d\n", null));
                str.append(String.format("Trip Match ID - %d\n", null));
                str.append(String.format("Trip Match ID - %d\n", null));
            }
            else {
                str.append("This trip request isn't matched yet\n");
            }
        }
        return str.toString();
    }

    private Integer getRequestTripID(TripRequest trip) {
        return tripRequestUtil.getTripID(trip);
    }

    public void addNewTripRequest(String input) {
        String[] inputs = input.split(",");
        TripRequest newRequest = new TripRequest(inputs[0], inputs[1], inputs[2], 12); // have to fix startingHour (parse to int somehow)
        tripRequestUtil.addRequestTrip(newRequest);
    }


    public boolean validateTripRequestInput(String input) {
        return tripRequestUtil.validateTripRequestInput(input);
    }

    public String getRequestValidationErrorMessage () {
        return tripRequestUtil.getValidationErrorMessage();
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

    public boolean validateMenuInput(String choice) {
        short input = 0;
        try {
            input = Short.parseShort(choice);
        }
        catch(Exception e) {
            try {
                Double.parseDouble(choice);
                menuErrorMessage = "Your choice was fraction (double) please choose Integer, try again";
                return false;
            }
            catch(Exception ex) {

            }
            menuErrorMessage = "Your choice isn't a number, please try again\n";
            return false;
        }
        if(input > 6 || input < 0 ) {
            menuErrorMessage = "Your choice isn't a number between 1-6, please try again\n";
            return false;
        }
        return true;
    }


    public String getMenuErrorMessage() {
        return menuErrorMessage;
    }

    public String getAllNotMatchedRequestsTrip() {
        StringBuilder str = new StringBuilder();
        int index = 1;

        if(tripRequestUtil.getAllRequestTrips().size() > 0) {
            str.append("Here are all the requests trip that didn't match yet");
        }
        else {
            str.append("There are no requests trip that didn't match yet");
            return str.toString();
        }

        for(Map.Entry<TripRequest, Integer> trip : tripRequestUtil.getAllRequestTrips().entrySet()) {
            if(!trip.getKey().isMatched()) {
                str.append(String.format("Request ID - %d\n", index, trip.getKey().getRequestID()));
                str.append(String.format("Name of requester - %s\n", trip.getKey().getNameOfOwner()));
                str.append(String.format("Source stations - %s\n", trip.getKey().getSourceStation()));
                str.append(String.format("Destination stations - %s\n", trip.getKey().getDestinationStation()));
                str.append(String.format("Starting hour - %s\n\n", trip.getKey().getStartingHour()));
                index++;
            }
        }
        return str.toString();
    }

    public boolean validateChooseRequestAndAmountOfSuggestedTripsInput(String input) {
        if(input.equals("b")) {
            return true;
        }
        else {
            String[] inputs = input.split(",");
            if(inputs.length != 2) {
                chooseRequestAndAmountOfSuggestedTripsErrorMessage.append("Please insert 2 elements, try again.\n");
            }
            if(checkIfStringIsInt(inputs[0]) && checkIfStringIsInt(inputs[1])) {
                if(validateRequestIDIsExist(inputs[0])) {
                    return true;
                }
                else {
                    chooseRequestAndAmountOfSuggestedTripsErrorMessage.append(String.format("Request Trip ID - %s isn't exist in the system, please try again\n", inputs[0]));
                    return false;
                }
            }
            else {
                chooseRequestAndAmountOfSuggestedTripsErrorMessage.append("Please insert two numbers (Integer), try again\n");
                return false;
            }
        }
    }

    private boolean validateRequestIDIsExist(String input) {
        Integer requestID = Integer.parseInt(input);
        return tripRequestUtil.isRequestIDExist(requestID);
    }

    public String getChooseRequestAndAmountOfSuggestedTripsErrorMessage() {
        return chooseRequestAndAmountOfSuggestedTripsErrorMessage.toString();
    }

    public void deleteNewTripRequestErrorMessage() {
        tripRequestUtil.deleteErrorMessage();
    }

    private boolean checkIfStringIsInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public String FindPotentialMatchToRequestTrip(String input) {
        StringBuilder str =  new StringBuilder();
        String[] inputs = input.split(",");
        int requestID = Integer.parseInt(inputs[0]);
        int suggestedAmountTrips = Integer.parseInt(inputs[1]);

        return "";
    }
}

