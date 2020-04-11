package UI.java;

import Engine.Manager.EngineManager;

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
                System.out.println(engineManager.LoadXML());
                break;
            }

            case 2: {
                System.out.println(engineManager.getAllStationsName());
                System.out.println("Please insert the following details:\n - Name of trip \n - Source station \n - Destination station \n - Starting time of trip \n ");
                String input = scanner.nextLine();
                break;
            }
            case 3:{
                System.out.println(engineManager.getAllTripOffers());
                break;
            }
            case 4: {
                System.out.println(engineManager.getAllTripRequests());
                break;
            }
            case 5: {

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
        str.append("Welcome to TransPool app \n");
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
