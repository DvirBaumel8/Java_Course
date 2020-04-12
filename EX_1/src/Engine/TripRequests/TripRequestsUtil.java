package Engine.TripRequests;

import Engine.Manager.EngineManager;
import Engine.XMLLoading.jaxb.schema.generated.Stop;

import java.util.HashMap;
import java.util.Map;

public class TripRequestsUtil {
    private Map<TripRequest, Integer> requestTrips;
    private StringBuilder validationErrorMessage;
    private static final String validationSuccessMessage = "Trip request added successfully\n";

    public Map<TripRequest, Integer> getRequestTrips() {
        return requestTrips;
    }

    private int nextRequestID;

    public TripRequestsUtil() {
        this.requestTrips = new HashMap<>();
        this.nextRequestID = 1;
        this.validationErrorMessage = new StringBuilder();
        this.validationErrorMessage.append("Sorry, your input was not valid. Errors: \n");
    }

    public void addRequestTrip(TripRequest requestTrip) {
        requestTrips.put(requestTrip, nextRequestID);
    }

    public boolean validateTripRequestInput(String input) {
        String[] inputs = input.split(",");
        boolean isValid = true;

        if(validateOwnerName(inputs[0])) {
            validationErrorMessage.append("*Request owner name is empty\n");
            isValid = false;
        }
        if(!validateSource(inputs[1])) {
            validationErrorMessage.append("*Source isn't exist in the system\n");
            isValid = false;
        }
        if(!validateDestination(inputs[2])) {
            validationErrorMessage.append("*Destination isn't exist in the system\n");
            isValid = false;
        }
        if(!validateStartingTime(inputs[3])) {
            validationErrorMessage.append("*Starting time isn't valid\n");
            isValid = false;
        }
        return isValid;
    }

    private boolean validateStartingTime (String input) {
        return true;
    }

    private boolean validateSource(String input) {
      return checkIFStationsIsExist(input);
    }

    private boolean validateOwnerName(String input) {
        return input == null || input.isEmpty();
    }

    private boolean validateDestination (String input) {
        return checkIFStationsIsExist(input);
    }

    private boolean checkIFStationsIsExist(String stationName) {
        for(Stop stop : EngineManager.getEngineManagerInstance().getTransPool().getMapDescriptor().getStops().getStop()) {
            if(stop.getName().equals(stationName)) {
                return true;
            }
        }
        return false;
    }

    public Integer getTripID(TripRequest trip) {
        return requestTrips.get(trip);
    }

    public String getValidationErrorMessage () {
        return validationErrorMessage.toString();
    }

    public String getValidationSuccessMessage () {
        return validationSuccessMessage;
    }
}
