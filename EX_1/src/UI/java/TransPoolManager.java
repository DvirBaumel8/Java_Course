package UI.java;

import java.util.Scanner;

public class TransPoolManager {
    public void run() {
        short userChoice = displayMenu();

        switch(userChoice) {
            case 1: break;
            case 2: break;
            case 3: break;
            case 4: break;
            case 5: break;
            case 6: break;
        }
    }

    private short displayMenu() {
        Scanner scanner = new Scanner(System.in);
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
