package Validations.TripValidations;

import TripSuggestUtil.TripSuggest;

import java.util.HashSet;

public class Validator {
    private static Validator instance;
    private StringBuilder menuErrorMessage;
    private RequestValidator requestValidator;
    private SuggestValidator suggestValidator;

    private Validator() {
        this.menuErrorMessage = new StringBuilder();
        this.requestValidator = new RequestValidator();
        this.suggestValidator = new SuggestValidator();
    }

    public static Validator getInstance() {
        if(instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    public boolean validateMenuInput(String choice) {
        boolean isValidInput = false;
        short input;
           try {
            input = Short.parseShort(choice);
           }
            catch(NumberFormatException ex) {
                this.menuErrorMessage.append("Please choose Integer, try again..\n");
                return false;
            }
            catch (Exception ex) {
                this.menuErrorMessage.append("Your choice isn't a number, please try again..\n");
                return false;
            }
            if(choice.length() > 1) {
                this.menuErrorMessage.append("Please insert only one digit, please try again..\n");
            }
           if(input > 7 || input < 1 ) {
               this.menuErrorMessage.append("Your choice isn't a number between 1-7, please try again..\n");
               isValidInput = false;
           }

           else {
               isValidInput = true;
           }

        return isValidInput;
    }

    public StringBuilder getMenuErrorMessage() {
        return menuErrorMessage;
    }

    public void setNullableMenuErrorMessage() {
        menuErrorMessage = new StringBuilder();
    }

    public boolean validateTripRequestInput(String[] inputs) {
        return requestValidator.validateTripRequestInput(inputs);
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

    public void deleteErrorMessageOfAddNewTripSuggest() {
        suggestValidator.deleteErrorMessageOfAddNewTripSuggest();
    }

    public String getChoosePotentialTripInputErrorMessage() {
        return requestValidator.getChoosePotentialTripInputErrorMessage();
    }

    public boolean validateChoosePotentialTripInput(String input, TripSuggest[] potentialSuggestedTrips) {
        return requestValidator.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
    }

    public boolean validateTripSuggestInput(String[] inputTripSuggestString, HashSet<String> allStationsLogicNames) {
        return suggestValidator.validateTripSuggestInput(inputTripSuggestString, allStationsLogicNames);
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
