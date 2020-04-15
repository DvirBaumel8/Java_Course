package UI;

import Engine.Manager.EngineManager;
import Engine.TripSuggestUtil.TripSuggest;

import java.util.List;
import java.util.Scanner;

public class TransPoolManager {
    private static EngineManager engineManager;
    private static TransPoolManager transPoolManagerInstance;
    private Scanner scanner = new Scanner(System.in);

    private TransPoolManager() {

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
                List<String> errors = null;
                System.out.println("Please copy your full path to master.xml file here for checking");
                String myPathToTheXMLFile = scanner.nextLine();
                System.out.println(engineManager.LoadXML(myPathToTheXMLFile, errors));

                if(errors != null) {
                    errors.forEach((error)-> { System.out.println(error+'\n'); });
                }
                run();
                break;
            }

            case 2: {
                String input = null;
                boolean isValidInput = false;
                String allStationsNames = engineManager.getAllStationsName();

                while(!isValidInput) {
                    System.out.println(allStationsNames);
                    System.out.println("Please insert the following details separated with ',' (Insert 'b' to go back to the main menu):\n - Name of owner \n - Source station \n - Destination station \n - Starting time of trip.");
                    input = scanner.nextLine();
                    isValidInput = engineManager.validateTripRequestInput(input);
                    if(isValidInput) {
                        if(input.equals("b")) {
                            run();
                            break;
                        }
                        System.out.println(engineManager.getRequestValidationSuccessMessage());
                    }
                    else {
                        System.out.println(engineManager.getRequestValidationErrorMessage());
                        engineManager.deleteNewTripRequestErrorMessage();
                    }
                }
                engineManager.addNewTripRequest(input);
                engineManager.deleteNewTripRequestErrorMessage();

                run();
                break;
            }

            case 3:{
                System.out.println(engineManager.getAllSuggestedTrips());
                run();
                break;
            }

            case 4: {
                System.out.println(engineManager.getAllTripRequests());
                run();
                break;
            }

            case 5: {
                boolean isValidInput = false;
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

            case 6: {
                System.exit(0);
                break;
            }
        }
    }

    private String getValidChooseOfSuggestedTrip(String input, TripSuggest[] potentialSuggestedTrips) {
        boolean isValid = false;
        String potentialSuggestedTripsStr = engineManager.convertPotentialSuggestedTripsToString(potentialSuggestedTrips);

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
        str.append("3. Display status of all suggested trips \n");
        str.append("4. Display status of all trip requests \n");
        str.append("5. Match trip request to trip suggest \n");
        str.append("6. Exit \n");

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
}
