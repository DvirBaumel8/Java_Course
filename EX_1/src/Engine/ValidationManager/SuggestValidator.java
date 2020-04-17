package Engine.ValidationManager;

import java.util.HashSet;
import java.util.Map;

public class SuggestValidator extends ActionValidator {
    private StringBuilder addNewTripSuggestErrorMessage = new StringBuilder();

    public static final int TRIP_SUGGEST_INPUT_LIMIT = 7;
    public static final int[] TRIP_SCHEDULE_TYPE_INPUT_RANGE  = {1, 5};

    public StringBuilder getAddNewTripSuggestErrorMessage() {
        return addNewTripSuggestErrorMessage;
    }

    public boolean validateTripSuggestInput(String input, HashSet<String> allStationsLogicNames) {
        String[] inputs = input.split(",");
        boolean isValid = true;
        //example of valid input - Ohad,ACB,3,13:25,4,30,2

        if (input.equals("b")) {
            return true;
        }
        if (inputs.length != TRIP_SUGGEST_INPUT_LIMIT) {
            addNewTripSuggestErrorMessage.append("Please insert 7 elements, try again.\n");
            return false;
        }
        if (!validateOwnerName(inputs[0])) {
            isValid = false;
        }
        if(!validateOwnerRoute(inputs[1], allStationsLogicNames)) {//add 1 more check of  valid route A.B.C
            isValid = false;
        }
        if (!validateDepartureDayNumber(inputs[2])) {
            isValid = false;
        }
        if (!validateStartingTime(inputs[3])) {
            //check if its : 24 (0-23) and minutes in multiples of 5 (0 - 55)
            isValid = false;
        }
        if (!validateTripScheduleType(inputs[4])) {
            isValid = false;
        }
        if (!validatePPK(inputs[5])) {
            // check again if how the calc if this value valid
            isValid = false;
        }
        if (!validatePossiblePassengerCapacity(inputs[6])) {
            isValid = false;
        }

        return isValid;
    }

    public boolean validateDepartureDayNumber(String input) {
        boolean res = false;
        int intInput = Integer.valueOf(input);

            if (isNumeric(input)) {
                if (intInput >= 1) {
                    res = true;
                }
            }
            if(!res) {
                addNewTripSuggestErrorMessage.append("Departure day number is not valid," +
                        " please try again, insert a number bigger than 0 \n");
            }

        return res;
        }

    public boolean validateTripScheduleType(String input) {
        boolean res = false;
        int intInput = Integer.valueOf(input);

        if (isNumeric(input)) {
            if (intInput >= TRIP_SCHEDULE_TYPE_INPUT_RANGE[0] && intInput <=TRIP_SCHEDULE_TYPE_INPUT_RANGE[1]) {
                res = true;
            }
        }
        if(!res) {
            addNewTripSuggestErrorMessage.append("Trip schedule type is not valid" +
                    "please try again, insert a number between 1 to 5 (include) \n");
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
        int intInput = Integer.valueOf(input);

        if (isNumeric(input)) {
            if (intInput >= 1) {
                return true;
            }
        }
       return false;
    }

    public void deleteErrorMessageOfAddNewTripSuggest () {
        addNewTripSuggestErrorMessage.setLength(0);
        addNewTripSuggestErrorMessage.append("\nSorry, your input was not valid. Errors: \n");
    }

    public boolean validateOwnerRoute(String route, HashSet<String> allStationsLogicNames) {
        String[] stations = route.split(".");
        HashSet<String> inputStations = new HashSet<>();
        boolean isStationsInputUnique = true;

        for(String station : stations) {
            if(inputStations.contains(station) || !allStationsLogicNames.contains(station)) {
                isStationsInputUnique = false;
            }
            else {
                inputStations.add(station);
            }
        }

        if(isStationsInputUnique) {
            return true;
        }
        else {
            return false;
        }

    }
}
