package Engine.Validations.TripValidations;

import Engine.TripSuggestUtil.TripSuggest;

import java.util.HashSet;

public class Validator {
    private static Validator instance;
    private StringBuilder menuErrorMessage;
    private RequestValidator requestValidator;
    private SuggestValidator suggestValidator;
    private boolean isXMLLoaded;

    private Validator() {
        this.menuErrorMessage = new StringBuilder();
        this.requestValidator = new RequestValidator();
        this.suggestValidator = new SuggestValidator();
        isXMLLoaded = false;
    }

    public static Validator getInstance() {
        if(instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    public boolean validateMenuInput(String choice) {
        short input;
        try {
            input = Short.parseShort(choice);
        }
        catch(Exception e) {
            try {
                Double.parseDouble(choice);
                this.menuErrorMessage.append("Your choice was fraction (double) please choose Integer, try again");
                return false;
            }
            catch(Exception ex) {

            }
            this.menuErrorMessage.append("Your choice isn't a number, please try again\n");
            return false;
        }
        if(input > 7 || input < 0 ) {
            this.menuErrorMessage.append("Your choice isn't a number between 1-7, please try again\n");
            return false;
        }
        if(!isXMLLoaded) {

        }
        return true;
    }

    public StringBuilder getMenuErrorMessage() {
        return menuErrorMessage;
    }

    public boolean validateTripRequestInput(String input) {
        return requestValidator.validateTripRequestInput(input);
    }

    public String getAddNewTripRequestErrorMessage() {
        return requestValidator.getAddNewTripRequestErrorMessage();
    }

    public boolean validateChooseRequestAndAmountOfSuggestedTripsInput(String input) {
        return requestValidator.validateChooseRequestAndAmountOfSuggestedTripsInput(input);
    }

    public String getChooseRequestAndAmountOfSuggestedTripsErrorMessage() {
        return requestValidator.getChooseRequestAndAmountOfSuggestedTripsErrorMessage();
    }

    public void deleteChooseRequestAndAmountErrorMessage() {
        requestValidator.deleteChooseRequestAndAmountErrorMessage();
    }

    public void deleteErrorMessageOfAddNewTripRequest() {
        requestValidator.deleteErrorMessageOfAddNewTripRequest();
    }

    public String getChoosePotentialTripInputErrorMessage() {
        return requestValidator.getChoosePotentialTripInputErrorMessage();
    }

    public boolean validateChoosePotentialTripInput(String input, TripSuggest[] potentialSuggestedTrips) {
        return requestValidator.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
    }

    public boolean validateTripSuggestInput(String input, HashSet<String> allStationsLogicNames) {
        return suggestValidator.validateTripSuggestInput(input, allStationsLogicNames);
    }

    public String getAddNewTripSuggestErrorMessage() {
        return suggestValidator.getAddNewTripSuggestErrorMessage();
    }

    public String getSuggestValidationSuccessMessage() {
        return suggestValidator.getSuggestValidationSuccessMessage();
    }

    public void deleteSuggestTripErrorMessage() {
        suggestValidator.deleteErrorMessage();
    }
}
