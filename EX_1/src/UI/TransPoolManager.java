package UI;

import Engine.DateSystem.DateSystemManger;
import Engine.Manager.EngineManager;
import Engine.TripSuggestUtil.TripSuggest;

import java.util.*;

public class TransPoolManager {
    private static EngineManager engineManager;
    private static TransPoolManager transPoolManagerInstance;
    private Scanner scanner = new Scanner(System.in);
    private boolean isXMLLoaded = false;
    DateSystemManger dateSystemManger = null;

    private TransPoolManager() {
        dateSystemManger = DateSystemManger.getInstance();
    }

    public static TransPoolManager getTransPoolManagerInstance() {
        if(transPoolManagerInstance == null) {
            transPoolManagerInstance = new TransPoolManager();
            engineManager = EngineManager.getEngineManagerInstance();
        }
        return transPoolManagerInstance;
    }

    public void run() {
        short userChoice = displayMenu();

        switch(userChoice) {
            case 1: {
                List<String> errors = new ArrayList<>();

                System.out.println("Please copy your full path to master.xml file.");

                String myPathToTheXMLFile = scanner.nextLine();
                errors = engineManager.LoadXML(myPathToTheXMLFile, errors);
                if(errors.size() == 0) {
                    System.out.println(engineManager.getXMLValidationsSuccessMessage());
                }
                else {
                    System.out.println(errors);
                }
                run();
                break;
            }

            case 2: {
                String input;
                boolean isValidInput = false;
                String allStationsNames = engineManager.getAllStationsName();
                while(!isValidInput) {
                    printAddNewTripRequestMenu(allStationsNames);
                    input = scanner.nextLine();
                    isValidInput = engineManager.validateTripRequestInput(input);
                    if(isValidInput) {
                        if(input.equals("b")) {
                            run();
                            break;
                        }
                        engineManager.addNewTripRequest(input);
                        System.out.println(engineManager.getRequestValidationSuccessMessage());
                    }
                    else {
                        System.out.println(engineManager.getRequestValidationErrorMessage());
                        engineManager.deleteNewTripRequestErrorMessage();
                    }
                }
                run();
                break;
            }

            case 3: {
                String input = null;
                boolean isValidInput = false;
                String allStationsNames = engineManager.getAllStationsName();
                HashSet<String> allStationsLogicNames = engineManager.getAllLogicStationsName();

                while(!isValidInput) {
                    printAddNewTripSuggestMenu(allStationsNames);
                    input = scanner.nextLine();
                    isValidInput = engineManager.validateTripSuggestInput(input, allStationsLogicNames);
                    if(isValidInput) {
                        if(input.equals("b")) {
                            run();
                            break;
                        }
                        engineManager.addNewTripSuggest(input);
                        System.out.println(engineManager.getSuggestValidationSuccesMessage());
                    }
                    else {
                        System.out.println(engineManager.getSuggestValidationErrorMessage());
                        engineManager.deleteSuggestTripValidationErrorMessage();
                    }
                }
                run();
                break;
            }

            case 4:{
                System.out.println(engineManager.getAllSuggestedTrips());
                run();
                break;
            }

            case 5: {
                System.out.println(engineManager.getAllTripRequests());
                run();
                break;
            }

            case 6: {
                String input, requestIDAndAmountToMatch;

                requestIDAndAmountToMatch = getValidRequestIDAndAmountToMatch();
                if(requestIDAndAmountToMatch.equals("b")) {
                    run();
                    break;
                }

                TripSuggest[] potentialSuggestedTrips = engineManager.findPotentialMatchToRequestTrip(requestIDAndAmountToMatch);
                if(potentialSuggestedTrips == null) {
                    System.out.println("Sorry, there is no potential trips to be matched for your request in the system, you will be transfer to the main menu.\n");
                    run();
                    break;
                }
                input = getValidChooseOfSuggestedTrip(requestIDAndAmountToMatch, potentialSuggestedTrips);
                System.out.println(engineManager.matchRequestToSuggest(input, potentialSuggestedTrips, requestIDAndAmountToMatch));
                run();
                break;
            }

            case 7: {
                System.exit(0);
                break;
            }
        }
    }


    private String getValidChooseOfSuggestedTrip(String requestIDAndAmount, TripSuggest[] potentialSuggestedTrips) {
        boolean isValid = false;
        String input = null;
        String[] inputs = requestIDAndAmount.split(",");

        String potentialSuggestedTripsStr = engineManager.convertPotentialSuggestedTripsToString(potentialSuggestedTrips, inputs[0]);

        while(!isValid) {
            System.out.println(potentialSuggestedTripsStr);
            System.out.println("Please choose one of the following potential suggested trips to be match for your request. Please insert the suggested trip ID.");
            input = scanner.nextLine();
            isValid = engineManager.validateChoosePotentialTripInput(input, potentialSuggestedTrips);
            if(!isValid) {
                System.out.println(engineManager.getChoosePotentialTripInputErrorMessage());
            }
        }

        return input;
    }

    private String getValidRequestIDAndAmountToMatch() {
        String input = null;
        boolean isValid = false;
        String allNotMatchRequestsTrips = engineManager.getAllNotMatchedRequestsTrip();

        while(!isValid) {
            System.out.println(allNotMatchRequestsTrips);
            System.out.println("Please choose one of the requests to get match, in addition choose the amount of suggested trips you want to be displayed separated with ','  For example: 1,4 (1 - request trip id, 4 - amount of suggested trips). Insert 'b' to go back to the main menu");
            input = scanner.nextLine();
            isValid = engineManager.validateChooseRequestAndAmountOfSuggestedTripsInput(input);

            if(!isValid) {
                System.out.println(engineManager.getChooseRequestAndAmountOfSuggestedTripsErrorMessage());
                engineManager.deleteChooseRequestAndAmountErrorMessage();
            }
        }

        return input;
    }

    private short displayMenu() {
        StringBuilder str = new StringBuilder();
        str.append("\nMenu \n");
        str.append("1. Load XML file \n");
        str.append("2. New trip request \n");
        str.append("3. New trip suggested \n");
        str.append("4. Display status of all suggested trips \n");
        str.append("5. Display status of all trip requests \n");
        str.append("6. Match trip request to trip suggest \n");
        str.append("7. Exit \n");

        String input = null;
        boolean isValidInput = false;

        while(!isValidInput) {
            System.out.println(str);
            input = scanner.nextLine();
            isValidInput = engineManager.validateMenuInput(input);
            if(!isValidInput) {
                System.out.println(engineManager.getMenuErrorMessage());
            }
        }

        return Short.parseShort(input);
    }

    private void printErrorList(List<String> errors) {
        if (errors != null) {
            System.out.println("Please note the error list and fix them:" + '\n');
            errors.forEach((error) -> {
                System.out.println(error + '\n');
            });
            System.out.println("Its your lucky Day! Please try again...");
        }
        else {
            this.isXMLLoaded = true;
            System.out.println("XML Load successfully without any errors! Please continue...");
        }
    }

    public static List<String> addAndValidErrorList(List<String> errors, String errorMessage) {
        List<String> res = null;
        if(errors == null) {
            res = new LinkedList<>();
            res.add(errorMessage);
            return res;
        }
        else {
            errors.add(errorMessage);
            return errors;
        }
    }

    public void printAddNewTripRequestMenu(String allStationsNames) {
        System.out.println(allStationsNames);
        System.out.println("Please insert the following details separated with ',' (Insert 'b' to go back to the main menu):\n - Name of owner \n - Source station \n - Destination station \n - Starting time of trip.\n- arrival time of the trip");
    }

    public void printAddNewTripSuggestMenu(String allStationsNames) {
        System.out.println(allStationsNames);
        System.out.println("Please insert the following details separated with ','" +
                " (Insert 'b' to go back to the main menu):\n" +
                "- Suggest trip owner name \n" +
                "- Route of suggested trip separate with '.') \n" +
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
                " EXAMPLE: Ohad,A.C.B,3,13:25,4,30,2\n");
    }

    public void printFailedLoadXMLMessage() {
        System.out.println("XML still doesnt loaded/doesnt loaded successfully," +
                " Please try to load XML option again");
    }
}
