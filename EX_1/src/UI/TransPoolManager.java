package UI;

import Engine.Manager.EngineManager;

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
                System.out.println(engineManager.LoadXML());
                run();
                break;
            }

            case 2: {
                System.out.println(engineManager.getAllStationsName());
                String input = null;
                boolean isValidInput = false;

                while(!isValidInput) {
                    System.out.println("Please insert the following details separated with ,:\n - Name of owner \n - Source station \n - Destination station \n - Starting time of trip");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    isValidInput = engineManager.validateTripRequestInput(input);
                    if(isValidInput) {
                        System.out.println(engineManager.getRequestValidationSuccessMessage());
                    }
                    else {
                        System.out.println(engineManager.getRequestValidationErrorMessage());
                    }
                }
                engineManager.addNewTripRequest(input);

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
        str.append("Menu \n");
        str.append("1. Load XML file \n");
        str.append("2. New trip request \n");
        str.append("3. Display status of all trip offers \n");
        str.append("4. Display status od all trip requests \n");
        str.append("5. Match trip request to trip offer \n");
        str.append("6. Exit \n");

        short choice;
        System.out.println(str);
        choice = scanner.nextShort();

        while(choice > 6 || choice < 0 ) {
            System.out.println("Illegal choice, please choose a legal number");
            System.out.println(str);
            choice = scanner.nextShort();
        }

        return choice;
    }
}
