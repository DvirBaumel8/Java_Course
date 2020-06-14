package Manager;

import MatchingUtil.RoadTrip;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;

import java.util.*;

public class TransPoolManager {
    private static EngineManager engineManager;
    private static TransPoolManager transPoolManagerInstance;

    private static boolean isXMLLoaded = false;

    public static final short INPUT_EXIT = 7;
    public static final short INPUT_LOAD_XML = 1;


    private TransPoolManager() {
    }

    public static TransPoolManager getTransPoolManagerInstance() {
        if (transPoolManagerInstance == null) {
            transPoolManagerInstance = new TransPoolManager();
            engineManager = EngineManager.getEngineManagerInstance();
        }
        return transPoolManagerInstance;
    }

    private String getValidChooseOfSuggestedTrip(String requestIDAndAmount, List<RoadTrip> potentialSuggestedTrips) {
        boolean isValid = false;
        String input = null;
        String[] inputs = requestIDAndAmount.split(",");

        String potentialSuggestedTripsStr = engineManager.convertPotentialSuggestedTripsToString(potentialSuggestedTrips, inputs[0]);
        isValid = engineManager.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
        if (!isValid) {
            System.out.println(engineManager.getChoosePotentialTripInputErrorMessage());
        }
        return input;
    }

    public TripRequest addNewTripRequestSuccess(String[] inputs) {
        TripRequest newRequest = engineManager.addNewTripRequest(inputs);
        return newRequest;
    }

    public TripSuggest addNewTripSuggestSuccess(String[] inputs) {
        TripSuggest newSuggest = engineManager.addNewTripSuggest(inputs);
        return newSuggest;
    }

    public String getAddNewTripRequestErrorMessage() {
        String errors = engineManager.getRequestValidationErrorMessage();
        engineManager.deleteNewTripRequestErrorMessage();
        return errors;
    }

    public String getAddNewTripSuggestErrorMessage() {
        String errors = engineManager.getSuggestValidationErrorMessage();
        engineManager.deleteNewTripSuggestErrorMessage();
        return errors;
    }

    public List<String> matchTripRequestToTripSuggestActions(String requestIDAndAmountToMatch) {
        List<String> matchingErrors = new LinkedList<>();
        String input = null;
        //List<RoadTrip> potentialSuggestedTrips = engineManager.findPotentialMatchToRequestTrip(requestIDAndAmountToMatch);
//        if (potentialSuggestedTrips == null) {
//            matchingErrors.add("Sorry, there is no potential trips to be matched for your request in the system");
//        } else {
//            input = getValidChooseOfSuggestedTrip(requestIDAndAmountToMatch, potentialSuggestedTrips);
//        }
        return matchingErrors;
    }

    public static EngineManager getEngineManager() {
        return engineManager;
    }

    public static void setEngineManager(EngineManager engineManager) {
        TransPoolManager.engineManager = engineManager;
    }

    public static boolean isXMLLoaded() {
        return isXMLLoaded;
    }

    public static void setIsXMLLoaded(boolean isXMLLoaded) {
        TransPoolManager.isXMLLoaded = isXMLLoaded;
    }

    public static short getInputExit() {
        return INPUT_EXIT;
    }

    public static short getInputLoadXml() {
        return INPUT_LOAD_XML;
    }
}
