package UI.java;

import Engine.Manager.EngineManager;
import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.util.List;
import java.util.Scanner;

public class TransPoolManager {
    private EngineManager engineManager = EngineManager.getEngineManagerInstance();
    private static TransPoolManager transPoolManager;
    Scanner scanner = new Scanner(System.in);

    private TransPoolManager() {

    }

    public static TransPoolManager getTransPoolManagerInstance() {
        if(transPoolManager == null) {
            transPoolManager = new TransPoolManager();
        }
        return transPoolManager;
    }

    public void run() {
        short userChoice = displayMenu();

        switch(userChoice) {
            case 1: {
                List<String> errors = null;
                System.out.println("Please copy your full path to master.xml file here for checking");
                Scanner sc = new Scanner(System.in);
                String myPathToTheXMLFile = sc.nextLine();
                System.out.println(engineManager.LoadXML(myPathToTheXMLFile,errors));
                if(errors != null) {
                    errors.forEach((error)-> {
                        System.out.println(error+'\n');
                    });
                }
                run();
                break;
            }

            case 2: {
                System.out.println(engineManager.getAllStationsName());
                System.out.println("Please insert the following details separated with ,:\n - Name of owner \n - Source station \n - Destination station \n - Starting time of trip \n ");
                String input = scanner.nextLine();
                boolean isValidInput = false;

                while(isValidInput == false) {
                    System.out.println("Please insert the following details separated with ,:\n - Name of owner \n - Source station \n - Destination station \n - Starting time of trip \n ");
                    input = scanner.nextLine();
                    isValidInput = engineManager.validateTripRequestInput(input);
                }
                engineManager.createNewTripRequest(input);
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

        while(choice >6 || choice < 0 ) {
            System.out.println("Illegal choice, please choose a legal number");
            System.out.println(str);
            choice = scanner.nextShort();
        }

        return choice;
    }
}
