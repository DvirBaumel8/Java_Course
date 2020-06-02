package Manager;

import TripSuggestUtil.TripSuggest;

import java.util.*;

public class TransPoolManager {
    private static EngineManager engineManager;
    private static TransPoolManager transPoolManagerInstance;
    private Scanner scanner = new Scanner(System.in);
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

    public static EngineManager getEngineManagerInstance() {
        if (engineManager == null) {
            engineManager = EngineManager.getEngineManagerInstance();
        }
        return engineManager;
    }

    public void run() {
        short userChoice = displayMenu();
        if (userChoice == INPUT_EXIT) {
            System.exit(0);
        }

        if (this.isXMLLoaded) {
            switch (userChoice) {
                case 1: {
                    System.out.println("XML file already loaded please continue \n");
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    String allStationsNames = engineManager.getAllStationsName();
                    HashSet<String> allStationsLogicNames = engineManager.getAllLogicStationsName();
                    addNewTripSuggestedActions(allStationsNames, allStationsLogicNames);
                    break;
                }
                case 4: {
                    System.out.println(engineManager.getAllSuggestedTrips());
                    break;
                }
                case 5: {
                    System.out.println(engineManager.getAllTripRequests());
                    break;
                }
                case 6: {
                    String requestIDAndAmountToMatch = getValidRequestIDAndAmountToMatch();
                    if (requestIDAndAmountToMatch.equals("b")) {
                        run();
                        break;
                    }
                    matchTripRequestToTripSuggestActions(requestIDAndAmountToMatch);
                    break;
                }
            }
        }

        else { //xml doesnt load yet/successfully
            List<String> xmlErrors = new ArrayList<>();
            try {
                checkIfXMLIndeedLoaded(userChoice);
                System.out.println("Please copy your full path to master.xml file and than press enter:");
                String myPathToTheXMLFile = scanner.nextLine();
                xmlErrors = engineManager.LoadXML(myPathToTheXMLFile, xmlErrors);
                this.isXMLLoaded = printXMLResultAction(xmlErrors);
            } catch (Exception e) {
                xmlErrors.add(e.getMessage());
            } finally {
                checkIfErrorsOccurredAndPrint(xmlErrors);
            }
        }
        run();
    }

    private String getValidChooseOfSuggestedTrip(String requestIDAndAmount, TripSuggest[] potentialSuggestedTrips) {
        boolean isValid = false;
        String input = null;
        String[] inputs = requestIDAndAmount.split(",");

        String potentialSuggestedTripsStr = engineManager.convertPotentialSuggestedTripsToString(potentialSuggestedTrips, inputs[0]);

        while (!isValid) {
            System.out.println(potentialSuggestedTripsStr);
            System.out.println("Please choose one of the following potential suggested trips to be match for your request.\n" +
                    "Please insert the suggested trip ID or 'b' to go back to the Main Menu:\n");
            input = scanner.nextLine();
            if (input.equals("b")) {
                run();
                break;
            }
            isValid = engineManager.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
            if (!isValid) {
                System.out.println(engineManager.getChoosePotentialTripInputErrorMessage());
            }
        }

        return input;
    }

    private String getValidRequestIDAndAmountToMatch() {
        String input = null;
        boolean isValid = false;

        while (!isValid) {
            printRequestMatchMenu();
            input = scanner.nextLine();
            isValid = engineManager.validateChooseRequestAndAmountOfSuggestedTripsInput(input);

            if (!isValid) {
                System.out.println(engineManager.getChooseRequestAndAmountOfSuggestedTripsErrorMessage());
                engineManager.deleteChooseRequestAndAmountErrorMessage();
            }
        }

        return input;
    }

    private void printRequestMatchMenu() {
        String allNotMatchRequestsTrips = engineManager.getAllNotMatchedRequestsTrip();
        System.out.println(allNotMatchRequestsTrips);
        System.out.println("Match your trip request in the following way:\n" +
                "Separated with , Trip request ID, Maximum suggested trips  you want to be display'\n" +
                "For example: 1,4 (1 - request trip id, 4 - Maximum of amount of suggested trips)\n" +
                "Insert 'b' to go back to the main menu...\n");
    }

    private short displayMenu() {
        StringBuilder str = new StringBuilder();
        str.append("Menu - Please choose one of the following options: \n");
        str.append("1. Load XML file \n");
        str.append("2. New trip request \n");
        str.append("3. New trip suggested \n");
        str.append("4. Display status of all suggested trips \n");
        str.append("5. Display status of all trip requests \n");
        str.append("6. Match trip request to trip suggest \n");
        str.append("7. Exit");

        String input = null;
        boolean isValidInput = false;

        while (!isValidInput) {
            System.out.println(str);
            input = scanner.nextLine();
            isValidInput = engineManager.validateMenuInput(input);
            if (!isValidInput) {
                System.out.println(engineManager.getMenuErrorMessage());
                engineManager.setNullableMenuErrorMessage();
            }
        }

        return Short.parseShort(input);
    }

    public void printAddNewTripRequestMenu(String allStationsNames) {
        System.out.println(allStationsNames);
        System.out.println("Please insert the following details separated with ',' (Insert 'b' to go back to the main menu):\n" +
                "- Name of owner\n" +
                "- Source station\n" +
                "- Destination station\n" +
                "- start or arrival time of the trip (**:** format every 5 min) \n" +
                "- s to choose start time, a to choose arrival time.\n" +
                "Example: Ohad§,A,C,12:25,s");
    }

    public void printAddNewTripSuggestMenu(String allStationsNames) {
        System.out.println(allStationsNames);
        System.out.println("Please insert the following details separated with ','" +
                " (Insert 'b' to go back to the main menu):\n" +
                "- Suggest trip owner name \n" +
                "- Route of suggested trip separate with '-') \n" +
                "- Arrival Day Number \n" +
                "- Starting Time: Hour at 24 (0 - 23) and minutes in multiples of 5 (0 - 55)\n" +
                "- Trip schedule type: \n" +
                "  * insert 1 - one time \n" +
                "  * insert 2 - daily \n" +
                "  * insert 3 - twice a week \n" +
                "  * insert 4 - weekly \n" +
                "  * insert 5 - monthly \n" +
                "- PPK: cost of trip per kilometer \n" +
                "- Passengers capacity\n" +
                "EXAMPLE: Ohad,A-B-C,3,13:25,4,30,2\n");
    }

    public boolean printXMLResultAction(List<String> errors) {
        boolean res = false;
        if (errors.size() == 0) {
            System.out.println(engineManager.getXMLValidationsSuccessMessage());
            res = true;
        }
        return res;
    }

    public void printAndAddNewTripSuggestSuccess(String input) {
        engineManager.addNewTripSuggest(input);
        System.out.println(engineManager.getSuggestValidationSuccesMessage());
    }

    public void printNewTripSuggestSuccessErrorMessage() {
        System.out.println("Not valid New Trip Suggest for the following reason:");
        System.out.println(engineManager.getSuggestValidationErrorMessage());
        engineManager.deleteSuggestTripValidationErrorMessage();
        System.out.println("Please try again");
    }

    public void addNewTripRequestSuccess() {
        String[] inputs = null;
        engineManager.addNewTripRequest(inputs);
        System.out.println(engineManager.getRequestValidationSuccessMessage());
    }

    public String getAddNewTripRequestErrorMessage() {
        String errors = engineManager.getRequestValidationErrorMessage();
        engineManager.deleteNewTripRequestErrorMessage();
        return errors;
    }

    public void addNewTripSuggestedActions(String allStationsNames, HashSet<String> allStationsLogicNames) {
        boolean isValidInput = false;
        while (!isValidInput) {
            printAddNewTripSuggestMenu(allStationsNames);
            String input = scanner.nextLine();
            try {
                isValidInput = engineManager.validateTripSuggestInput(input, allStationsLogicNames);
                if (isValidInput) {
                    if (input.equals("b")) {
                        run();
                        break;
                    }
                    printAndAddNewTripSuggestSuccess(input);
                }
                else {
                    printNewTripSuggestSuccessErrorMessage();
                }
            }
            catch (Exception e) {
                printNewTripSuggestSuccessErrorMessage();
            }
        }
    }


    public void matchTripRequestToTripSuggestActions(String requestIDAndAmountToMatch) {
        String input = null;
        TripSuggest[] potentialSuggestedTrips = engineManager.findPotentialMatchToRequestTrip(requestIDAndAmountToMatch);
        if (potentialSuggestedTrips == null) {
            System.out.println("Sorry, there is no potential trips to be matched for your request in the system, you will be transfer to the main menu.\n");
        } else {
            input = getValidChooseOfSuggestedTrip(requestIDAndAmountToMatch, potentialSuggestedTrips);
            System.out.println(engineManager.matchRequestToSuggest(input, potentialSuggestedTrips, requestIDAndAmountToMatch));
        }
    }

    public void checkIfErrorsOccurredAndPrint(List<String> xmlErrors) {
        if (!xmlErrors.isEmpty()) {
            System.out.println("xml doesnt load successfully because the following reasons:");
            xmlErrors.forEach(System.out::print);
            System.out.println("\n");
        }

    }

    public void checkIfXMLIndeedLoaded(short userChoice){
        if(userChoice != INPUT_LOAD_XML) {
            if(!this.isXMLLoaded) {
                System.out.println("You cant start using the application without load the xml !!!");
            }
        }
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
