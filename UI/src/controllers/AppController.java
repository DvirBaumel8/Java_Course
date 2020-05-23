package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class AppController {
    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private TripRequestController tripRequestController;

    @FXML private TripSuggestController tripSuggestController;

    @FXML
    public void initialize() {
        if (headerComponentController != null && tripRequestController != null && tripSuggestController != null) {
            headerComponentController.setMainController(this);
            tripRequestController.setMainController(this);
            tripSuggestController.setMainController(this);
        }
    }

    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setTripRequestComponentController(TripRequestController tripRequestController) {
        this.tripRequestController = tripRequestController;
        this.tripRequestController.setMainController(this);
    }

    public void setTripSuggestComponentController(TripSuggestController tripSuggestController) {
        this.tripSuggestController = tripSuggestController;
        this.tripSuggestController.setMainController(this);
    }

    public void exitButtonAction() {
        headerComponentController.exitButtonActionListener();
    }

    public void loadXMLAction() {
        headerComponentController.loadXMLButtonActionListener();
    }

    public void addTripRequestAction() {
        tripRequestController.addTripRequestButtonActionListener();
    }

    public void addTripSuggestAction() {
        tripSuggestController.addTripSuggestButtonActionListener();
    }
}
