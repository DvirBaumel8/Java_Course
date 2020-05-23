package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TripRequestController {
    private AppController mainController;

    @FXML
    private Button addTripRequestButton;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void addTripRequestButtonActionListener() {
        mainController.addTripRequestAction();
    }
}
