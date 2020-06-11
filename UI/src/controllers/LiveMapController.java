package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LiveMapController {
    private AppController mainController;

    @FXML
    private Label timeButton;

    @FXML
    private Button fiveMinButton;

    @FXML
    private Button halfHourMinButton;

    @FXML
    private Button oneHourButton;

    @FXML
    private Button twoHoursButton;

    @FXML
    private Button oneDayButton;

    @FXML
    private Accordion currentTripsAccordion;


    @FXML
    void fiveMinButtonActionListener() {

    }

    @FXML
    void halfHourMinButtonActionListener() {
    }

    @FXML
    void oneHourButtonActionListener() {

    }

    @FXML
    void twoHoursButtonActionListener() {

    }

    @FXML
    void oneDayButtonActionListener() {

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
