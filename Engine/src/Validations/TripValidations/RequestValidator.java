package Validations.TripValidations;

import Manager.EngineManager;
import TripSuggestUtil.TripSuggest;

public class RequestValidator extends ActionValidator {
    private StringBuilder addNewTripRequestErrorMessage;
    private StringBuilder chooseRequestAndAmountOfSuggestedTripsErrorMessage;
    private String choosePotentialTripInputErrorMessage;

    private static final int TRIP_REQUEST_INPUT_LIMIT = 5;

    public RequestValidator() {
        this.addNewTripRequestErrorMessage = new StringBuilder();
        this.addNewTripRequestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
        this.chooseRequestAndAmountOfSuggestedTripsErrorMessage = new StringBuilder();
    }

    public boolean validateTripRequestInput(String[] inputs) {
        boolean isValid = true;

        if(inputs.length != TRIP_REQUEST_INPUT_LIMIT) {
            addNewTripRequestErrorMessage.append("Please insert 5 elements, try again.\n");
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

        addNewTripRequestErrorMessage.append(this.getGeneralErrorMessage());
        this.setGeneralErrorMessage(new StringBuilder());

        return isValid;
    }

    private boolean validateTimeParam(String input) {
        if(input.equals("s") || input.equals("a")) {
            return true;
        }
        else {
            return false;
        }

    }

    public String getAddNewTripRequestErrorMessage() {
        return addNewTripRequestErrorMessage.toString();
    }

    public boolean validateChooseRequestAndAmountOfSuggestedTripsInput(String input) {
        if(input.equals("b")) {
            return true;
        }
        else {
            String[] inputs = input.split(",");
            if(inputs.length != 2) {
                chooseRequestAndAmountOfSuggestedTripsErrorMessage.append("Please insert 2 elements, try again.\n");
                return false;
            }
            if(checkIfStringIsInt(inputs[0]) && checkIfStringIsInt(inputs[1])) {
                if(EngineManager.getEngineManagerInstance().validateRequestIDIsExist(inputs[0])) {
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

    public boolean validateChoosePotentialTripInput(String input, TripSuggest[] potentialSuggestedTrips) {
        int tripSuggestID = -1;
        try {
            tripSuggestID = Integer.parseInt(input);
        }
        catch(Exception e) {
            choosePotentialTripInputErrorMessage = "Your input isn't a number, please try again.";
        }
        for(int i =0; i < potentialSuggestedTrips.length; i++) {
            if(potentialSuggestedTrips[i].getSuggestID() == tripSuggestID) {
                return true;
            }
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
