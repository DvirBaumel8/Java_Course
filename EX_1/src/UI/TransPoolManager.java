package UI;

import Engine.Manager.EngineManager;

import java.util.List;
import java.util.Scanner;

public class TransPoolManager {
    private static EngineManager engineManager;
    private static TransPoolManager transPoolManagerInstance;
    Scanner scanner = new Scanner(System.in);

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
                System.out.println(engineManager.getAllStationsName());
                String input = null;
                boolean isValidInput = false;

                while(!isValidInput) {
                    System.out.println("Please insert the following details separated with ,   (Insert 'b' to go back to the main menu):\n - Name of owner \n - Source station \n - Destination station \n - Starting time of trip.");
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
                System.out.println(engineManager.getAllNotMatchedRequestsTrip());
                boolean isValidInput = true;
                String input;

                while(isValidInput) {
                    System.out.println("Please choose one of the requests to get match, in addition choose the amount of suggested trips you want to be displayed separated with ','  For example: 1(request ID) ,4 (amount of suggested trips). Insert 'b' to go back to the main menu");
                    input = scanner.nextLine();
                    isValidInput = engineManager.validateChooseRequestAndAmountOfSuggestedTripsInput(input);
                    if(!isValidInput) {
                        System.out.println(engineManager.getChooseRequestAndAmountOfSuggestedTripsErrorMessage());
                    }
                    else {
                        if(input.equals("b")) {
                            run();
                            break;
                        }
                        else {
                            String potentialMatches = ￿engineManager.FindPotentialMatchToRequestTrip(input);
                        }
                    }
                }
                run();
                break;
            }
            case 6: {
                System.exit(0);
                break;
            }
        }
    }

    private short displayMenu() {
        StringBuilder str = new StringBuilder();
        str.append("\nMenu \n");
        str.append("1. Load XML file \n");
        str.append("2. New trip request \n");
        str.append("3. Display status of all trip offers \n");
        str.append("4. Display status of all trip requests \n");
        str.append("5. Match trip request to trip offer \n");
        str.append("6. Exit \n");

        String input = null;
        short choice;
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
