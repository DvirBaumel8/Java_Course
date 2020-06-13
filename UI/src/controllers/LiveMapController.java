package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LiveMapController {
    private AppController mainController;

    //two types actions prev and forward

    @FXML private Label timeLabel;

    @FXML private Button forwardPreviousButton;
    boolean isForward = true;

    @FXML private Button fiveMinButton;

    @FXML private Button halfHourMinButton;

    @FXML private Button oneHourButton;

    @FXML private Button twoHoursButton;

    @FXML private Button oneDayButton;

    @FXML private Accordion currentTripsAccordion;

    @FXML
    void fiveMinButtonActionListener() {
        String timeToShow = mainController.setDateString5Min(this.isForward);
        //this.setDateSystemMangerString(timeToShow);
    }

    @FXML
    void halfHourMinButtonActionListener() {
        String timeToShow = mainController.setDateString30Min(this.isForward);
        //this.setDateSystemMangerString(timeToShow);
    }

    @FXML
    void oneHourButtonActionListener() {
        String timeToShow = mainController.setDateString1Hour(this.isForward);
        //this.setDateSystemMangerString(timeToShow);
    }

    @FXML
    void twoHoursButtonActionListener() {
        String timeToShow = mainController.setDateString2Hours(this.isForward);
        //this.setDateSystemMangerString(timeToShow);
    }

    @FXML
    void oneDayButtonActionListener() {
        String timeToShow = mainController.setDateString1Day(this.isForward);
       // this.setDateSystemMangerString(timeToShow);
    }

    @FXML
    void forwardPreviousButtonActionListener() {
        isForward = !isForward;
        if(isForward) {
            forwardPreviousButton.setText("Forward");
        }
        else {
            forwardPreviousButton.setText("Previous");
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    public void setDateSystemMangerString(String time) {
        timeLabel.setText("Time:"+time);
    }

    public void updateCurrentTripsAccordion(String suggestId, String currStation) {
        String matchingTextArea = currentTripsAccordion.getPanes().get(0).getText();
        StringBuilder matchingTextAreaBuilder = new StringBuilder(matchingTextArea);
        matchingTextAreaBuilder.append(suggestId + ',' + currStation + System.lineSeparator());
        TitledPane matchingTitledPane= new TitledPane("Current Trips", new TextArea(matchingTextAreaBuilder.toString()));
        currentTripsAccordion.getPanes().set(0,matchingTitledPane);
    }
}
