package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TripSuggestController {
    private AppController mainController;

    @FXML
    private Button addTripSuggest;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    @FXML
    void addTripSuggestButtonActionListener(ActionEvent event) {
        mainController.addTripSuggestAction();
    }
}
