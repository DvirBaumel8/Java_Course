package controllers;

import javafx.event.ActionEvent;
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
    void addTripRequestButtonActionListener(ActionEvent event) {
        mainController.addTripRequestAction();
    }


}
