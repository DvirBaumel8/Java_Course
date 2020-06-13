package Validations.TripValidations;

import Manager.EngineManager;
import MatchingUtil.RoadTrip;
import java.util.LinkedList;
import java.util.List;

public class RequestValidator extends ActionValidator {
    private StringBuilder addNewTripRequestErrorMessage;
    private StringBuilder chooseRequestAndAmountOfSuggestedTripsErrorMessage;
    private String choosePotentialTripInputErrorMessage;

    private static final int TRIP_REQUEST_INPUT_LIMIT = 6;

    public RequestValidator() {
        this.addNewTripRequestErrorMessage = new StringBuilder();
        this.addNewTripRequestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
        this.chooseRequestAndAmountOfSuggestedTripsErrorMessage = new StringBuilder();
    }

    public boolean validateTripRequestInput(String[] inputs) {
        boolean isValid = true;

        if(inputs.length != TRIP_REQUEST_INPUT_LIMIT) {
            addNewTripRequestErrorMessage.append("Please insert 6 elements, try again.\n");
            return false;
        }
        if(!super.validateOwnerName(inputs[0])) {
            isValid = false;
        }
        if(!(inputs[1].equals(inputs[2]))) {//dest and source stations are not the same
            if(!validateSource(inputs[1])) {
                isValid = false;
            }
            if(!validateDestination(inputs[2])) {
                isValid = false;
            }
        }
        else {
            addNewTripRequestErrorMessage.append("You entered same Source station and Destination station!!\n");
            isValid = false;
        }
        if(!super.validateTime(inputs[3], 3)) {
            isValid = false;
        }

        if(!validateTimeParam(inputs[4])) {
            addNewTripRequestErrorMessage.append("The fifth parameter is invalid, please insert a to choose arrival time or s to choose starting time.");
            isValid = false;
        }
        if(!validateTripDay(inputs[5])) {
            isValid = false;
        }


        addNewTripRequestErrorMessage.append(this.getGeneralErrorMessage());
        this.setGeneralErrorMessage(new StringBuilder());

        return isValid;
    }

    private boolean validateTripDay(String input) {
        int dayN;
        try {
            dayN = Integer.parseInt(input);
        }
        catch (Exception e) {
            addNewTripRequestErrorMessage.append("Trip request Day number isn't an integer.");
            return false;
        }
        if(dayN >= 1) {
            return true;
        }
        else {
            addNewTripRequestErrorMessage.append("Trip request Day number isn't a day >= 1.");
            return false;
        }
    }

    private boolean validateTimeParam(String input) {
        if(input.equals("S") || input.equals("A")) {
            return true;
        }
        else {
            return false;
        }

    }

    public String getAddNewTripRequestErrorMessage() {
        return addNewTripRequestErrorMessage.toString();
    }

    public List<String> validateChooseRequestAndAmountOfSuggestedTripsInput(String input) {
        List<String> matchingErrors = new LinkedList<>();

        String[] inputs = input.split(",");
        if(inputs.length != 2) {
            matchingErrors.add("Please insert 2 elements, try again.\n");
        }
        if(checkIfStringIsInt(inputs[0]) && checkIfStringIsInt(inputs[1])) {
            if(!EngineManager.getEngineManagerInstance().validateRequestIDIsExist(inputs[0])) {
                matchingErrors.add(String.format("Request Trip ID - %s isn't exist in the system, please try again", inputs[0]));
            }
        }
        else {
            matchingErrors.add(String.format("Please insert two numbers (Integer), try again"));
        }
        return matchingErrors;
    }


    public String getChooseRequestAndAmountOfSuggestedTripsErrorMessage() {
        return chooseRequestAndAmountOfSuggestedTripsErrorMessage.toString();
    }

    public void deleteErrorMessageOfAddNewTripRequest () {
        addNewTripRequestErrorMessage.setLength(0);
        addNewTripRequestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
    }

    public void deleteChooseRequestAndAmountErrorMessage () {
        chooseRequestAndAmountOfSuggestedTripsErrorMessage.setLength(0);
    }

    public String getChoosePotentialTripInputErrorMessage() {
        return choosePotentialTripInputErrorMessage;
    }

    public boolean validateChoosePotentialTripInput(String input, List<RoadTrip> potentialSuggestedTrips) {
        int tripSuggestID = -1;
        try {
            tripSuggestID = Integer.parseInt(input);
        }
        catch(Exception e) {
            choosePotentialTripInputErrorMessage = "Your input isn't a number, please try again.";
        }
        for(int i =0; i < potentialSuggestedTrips.size(); i++) {
//            if(potentialSuggestedTrips.get(i).getSuggestID() == tripSuggestID) {
//                return true;
//            }
        }
        choosePotentialTripInputErrorMessage = "Your choice isn't one of the suggested trips ID's, please try again";
        return false;
    }

    public boolean validateSource(String input) {
        if(checkIFStationsIsExist(input)) {
            return true;
        }
        else {
            addNewTripRequestErrorMessage.append("Source isn't exist in the system\n");
            return false;
        }
    }
}
