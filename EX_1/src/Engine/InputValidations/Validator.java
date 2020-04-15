package Engine.InputValidations;

import Engine.Manager.EngineManager;
import Engine.TripSuggestUtil.TripSuggest;
import Engine.XMLLoading.jaxb.schema.generated.Stop;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private StringBuilder addNewTripRequestErrorMessage;
    private String menuErrorMessage;
    private StringBuilder chooseRequestAndAmountOfSuggestedTripsErrorMessage;
    private String choosePotentialTripInputErrorMessage;

    public Validator() {
        this.addNewTripRequestErrorMessage = new StringBuilder();
        this.addNewTripRequestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
        this.chooseRequestAndAmountOfSuggestedTripsErrorMessage = new StringBuilder();
    }

    public boolean validateTripRequestInput(String input) {
        String[] inputs = input.split(",");
        boolean isValid = true;

        if(input.equals("b")) {
            return true;
        }
        if(inputs.length != 4) {
            addNewTripRequestErrorMessage.append("Please insert 4 elements, try again.\n");
            return false;
        }
        if(!validateOwnerName(inputs[0])) {
            isValid = false;
        }
        if(!validateSource(inputs[1])) {
            isValid = false;
        }
        if(!validateDestination(inputs[2])) {
            isValid = false;
        }
        if(!validateStartingTime(inputs[3])) {
            isValid = false;
        }
        return isValid;
    }

    private boolean validateStartingTime (String input) {
        Pattern p = Pattern.compile("[0-2][0-9]:[0-5][0-9]");
        Matcher matcher = p.matcher(input);
        if(matcher.matches()) {
            return true;
        }
        else {
            addNewTripRequestErrorMessage.append("*Time template isn't valid, template should be __:__ (12:34)\n");
            return false;
        }
    }

    private boolean validateSource(String input) {
        if(checkIFStationsIsExist(input)) {
            return true;
        }
        else {
            addNewTripRequestErrorMessage.append("*Source isn't exist in the system\n");
            return false;
        }
    }

    private boolean validateOwnerName(String input) {
        try {
            Integer.parseInt(input);
            addNewTripRequestErrorMessage.append("*Owner name can't contains only numbers\n");
        }
        catch(Exception e) {
            return true;
        }
        if(input.isEmpty()) {
            addNewTripRequestErrorMessage.append("*Request owner name is empty\n");
            return false;
        }
        return true;
    }

    private boolean validateDestination (String input) {
        if(checkIFStationsIsExist(input)) {
            return true;
        }
        else {
            addNewTripRequestErrorMessage.append("*Destination isn't exist in the system\n");
            return false;
        }
    }

    private boolean checkIFStationsIsExist(String stationName) {
        for(Stop stop : EngineManager.getEngineManagerInstance().getTransPool().getMapDescriptor().getStops().getStop()) {
            if(stop.getName().equals(stationName)) {
                return true;
            }
        }
        return false;
    }

    public String getAddNewTripRequestErrorMessage() {
        return addNewTripRequestErrorMessage.toString();
    }

    public void deleteErrorMessageOfAddNewTripRequest () {
        addNewTripRequestErrorMessage.setLength(0);
        addNewTripRequestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
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

    private boolean checkIfStringIsInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public String getChooseRequestAndAmountOfSuggestedTripsErrorMessage() {
        return chooseRequestAndAmountOfSuggestedTripsErrorMessage.toString();
    }

    public void deleteChooseRequestAndAmountErrorMessage () {
        chooseRequestAndAmountOfSuggestedTripsErrorMessage.setLength(0);
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

    public String getChoosePotentialTripInputErrorMessage() {
        return choosePotentialTripInputErrorMessage;
    }

}
