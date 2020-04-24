package UI;

import Engine.Manager.EngineManager;
import Engine.TripSuggestUtil.TripSuggest;

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
                    String allStationsNames = engineManager.getAllStationsName();
                    addNewTripRequestActions(allStationsNames);
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
                    String requestIDAndAmountToMatch = null;
                    requestIDAndAmountToMatch = getValidRequestIDAndAmountToMatch();
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
            System.out.println("Please choose one of the following potential suggested trips to be match for your request. Please insert the suggested trip ID.");
            input = scanner.nextLine();
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
        String allNotMatchRequestsTrips = engineManager.getAllNotMatchedRequestsTrip();

        while (!isValid) {
            System.out.println(allNotMatchRequestsTrips);
            System.out.println("Please choose one of the requests to get match, in addition choose the amount of suggested trips you want to be displayed separated with ','  For example: 1,4 (1 - request trip id, 4 - amount of suggested trips). Insert 'b' to go back to the main menu");
            input = scanner.nextLine();
            isValid = engineManager.validateChooseRequestAndAmountOfSuggestedTripsInput(input);

            if (!isValid) {
                System.out.println(engineManager.getChooseRequestAndAmountOfSuggestedTripsErrorMessage());
                engineManager.deleteChooseRequestAndAmountErrorMessage();
            }
        }

        return input;
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
            }
        }

        return Short.parseShort(input);
    }

    public void printAddNewTripRequestMenu(String allStationsNames) {
        System.out.println(allStationsNames);
        System.out.println("Please insert the following details separated with ',' (Insert 'b' to go back to the main menu):\n " +
                "- Name of owner\n" +
                "- Source station\n" +
                "- Destination station\n" +
                "- Starting time of trip\n" +
                "- arrival time of the trip");
    }

    public void printAddNewTripSuggestMenu(String allStationsNames) {
        System.out.println(allStationsNames);
        System.out.println("Please insert the following details separated with ','" +
                " (Insert 'b' to go back to the main menu):\n" +
                "- Suggest trip owner name \n" +
                "- Route of suggested trip separate with '-') \n" +
                "- Arrival Day Number \n" +
                "- Arrival Time: Hour at 24 (0 - 23) and minutes in multiples of 5 (0 - 55)\n" +
                "- Trip schedule type: \n" +
                "  * insert 1 - one time \n" +
                "  * insert 2 - daily \n" +
                "  * press 3 - twice a week \n" +
                "  * press 4 - weekly \n" +
                "  * press 5 - monthly \n" +
                "- PPK: cost of trip per kilometer \n" +
                "- Passengers capacity\n" +
                " EXAMPLE: Ohad,A-C-B,3,13:25,4,30,2\n");
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

    public void printAndAddNewTripRequestSuccess(String input) {
        engineManager.addNewTripRequest(input);
        System.out.println(engineManager.getRequestValidationSuccessMessage());
    }

    public void printNewNewTripRequestErrorMessage() {
        System.out.println(engineManager.getRequestValidationErrorMessage());
        engineManager.deleteNewTripRequestErrorMessage();
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

    void addNewTripRequestActions(String allStationsNames) {
        boolean isValidInput = false;
        while (!isValidInput) {
            printAddNewTripRequestMenu(allStationsNames);
            String input = scanner.nextLine();
            try {
                isValidInput = engineManager.validateTripRequestInput(input);
                if (isValidInput) {
                    if (input.equals("b")) {
                        run();
                        break;
                    }
                    printAndAddNewTripRequestSuccess(input);
                } else {
                    printNewNewTripRequestErrorMessage();
                }
            }
            catch (Exception e) {
                printNewNewTripRequestErrorMessage();
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
            xmlErrors.forEach((xmlError) -> {
                System.out.println(xmlError);
            });
        }
    }

    public void checkIfXMLIndeedLoaded(short userChoice){
        if(userChoice != INPUT_LOAD_XML) {
            if(!this.isXMLLoaded) {
                System.out.println("You cant start using the application without load the xml !!!");
            }
        }
    }
}
