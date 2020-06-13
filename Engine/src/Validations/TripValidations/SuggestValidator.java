package Validations.TripValidations;

import java.util.HashSet;

public class SuggestValidator extends ActionValidator {
    private StringBuilder addNewTripSuggestErrorMessage = new StringBuilder();
    private static final int TRIP_SUGGEST_INPUT_LIMIT = 7;
    private static final int[] TRIP_SCHEDULE_TYPE_INPUT_RANGE  = {1, 5};
    private static final String SUCCESS_MESSAGE = "Trip suggest was added successfully to the system.\n";

    public String getAddNewTripSuggestErrorMessage() {
        return addNewTripSuggestErrorMessage.toString();
    }

    public boolean validateTripSuggestInput(String[] inputTripSuggestString, HashSet<String> allStationsLogicNames) {
        boolean isValid = true;
        //example of valid input - Ohad,A.C.B,3,13:25,4,30,2

        if (inputTripSuggestString.length != TRIP_SUGGEST_INPUT_LIMIT) {
            addNewTripSuggestErrorMessage.append("Please insert 7 elements, try again.\n");
            return false;
        }
        if (!validateOwnerName(inputTripSuggestString[0])) {
            isValid = false;
        }
        if(!validateOwnerRoute(inputTripSuggestString[1], allStationsLogicNames)) {//add 1 more check of valid route A.B.C
            addNewTripSuggestErrorMessage.append("Not valid route.\n");
            isValid = false;
        }
        if (!validateDepartureDayNumber(inputTripSuggestString[2])) {
            isValid = false;
        }
        if (!validateTime(inputTripSuggestString[3], 1)) {
            //check if its : 24 (0-23) and minutes in multiples of 5 (0 - 55)
            isValid = false;
        }
        if (!validateTripScheduleType(inputTripSuggestString[4])) {
            isValid = false;
        }
        if (!validatePPK(inputTripSuggestString[5])) {
            // check again if how the calc if this value valid
            isValid = false;
        }
        if (!validatePossiblePassengerCapacity(inputTripSuggestString[6])) {
            isValid = false;
        }

        addNewTripSuggestErrorMessage.append(this.getGeneralErrorMessage());
        this.setGeneralErrorMessage(new StringBuilder());

        return isValid;
    }

    public boolean validateDepartureDayNumber(String input) {
        boolean res = false;
        try {
            int intInput = Integer.parseInt(input);
            if (isNumeric(input)) {
                if (intInput >= 1) {
                    res = true;
                }
            }
            if(!res) {
                addNewTripSuggestErrorMessage.append("Arrival day number is not valid," +
                        " please try again, insert a number bigger than 0 \n");
            }
        }
        catch (NumberFormatException e) {
            addNewTripSuggestErrorMessage.append(e.getMessage());
        }

        return res;
    }

    public boolean validateTripScheduleType(String input) {
        boolean res = false;
        try {
            int intInput = Integer.parseInt(input);
            if (isNumeric(input)) {
                if (intInput >= TRIP_SCHEDULE_TYPE_INPUT_RANGE[0] && intInput <=TRIP_SCHEDULE_TYPE_INPUT_RANGE[1]) {
                    res = true;
                }
            }
            if(!res) {
                addNewTripSuggestErrorMessage.append("Trip schedule type is not valid" +
                        "please try again, insert a number between 1 to 5 (include) \n");
            }
        }
        catch (Exception e) {

        }

        return res;
    }

    public boolean validatePPK(String input) {
        boolean res = false;
        res = checkIfANumberAndBiggerThanOne(input);

        if(!res) {
            addNewTripSuggestErrorMessage.append("PPK number is not valid," +
                    " please try again, insert a number bigger than 0 \n");
        }
        return res;
    }

    public void deleteErrorMessageOfAddNewTripSuggest () {
        addNewTripSuggestErrorMessage.setLength(0);
        addNewTripSuggestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
    }

    public boolean validatePossiblePassengerCapacity(String input) {
        boolean res = false;
        res = checkIfANumberAndBiggerThanOne(input);

        if(!res) {
            addNewTripSuggestErrorMessage.append("Possible passenger capacity number is not valid," +
                    " please try again, insert a number bigger than 0 \n");
        }
        return res;
    }

    public static boolean isNumeric(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        return str.chars().allMatch(Character::isDigit);
    }

    public boolean checkIfANumberAndBiggerThanOne(String input) {
        int intInput = Integer.parseInt(input);

        if (isNumeric(input)) {
            if (intInput >= 1) {
                return true;
            }
        }
        return false;
    }

    public boolean validateOwnerRoute(String route, HashSet<String> allStationsLogicNames) {
        String[] stations = route.split("-");
        HashSet<String> inputStations = new HashSet<>();
        boolean isStationsInputUnique = true;

        for(String station : stations) {
            if(!inputStations.contains(station)) {
                inputStations.add(station);
            }
            else {
                isStationsInputUnique = false;
            }
        }

        return isStationsInputUnique;

    }

    public String getSuggestValidationSuccessMessage() {
        return SUCCESS_MESSAGE;
    }

    public void deleteErrorMessage() {
        addNewTripSuggestErrorMessage.setLength(0);
        addNewTripSuggestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
    }
}
