package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LiveMapController {
    private AppController mainController;

    //two types actions prev and forward

    @FXML private Label timeLabel;

    @FXML private Button forwardPreviousButton;
    private boolean isForward = true;

    @FXML private Button fiveMinButton;

    @FXML private Button halfHourMinButton;

    @FXML private Button oneHourButton;

    @FXML private Button twoHoursButton;

    @FXML private Button oneDayButton;

    @FXML private Accordion currentTripsAccordion;

    @FXML
    void fiveMinButtonActionListener() {
        mainController.setDateString5Min(isForward);
        setTimeLabel(mainController.getCurrentTime());
    }

    @FXML
    void halfHourMinButtonActionListener() {
        mainController.setDateString30Min(isForward);
        setTimeLabel(mainController.getCurrentTime());
    }

    @FXML
    void oneHourButtonActionListener() {
        mainController.setDateString1Hour(isForward);
        setTimeLabel(mainController.getCurrentTime());
    }

    @FXML
    void twoHoursButtonActionListener() {
        mainController.setDateString2Hours(isForward);
        setTimeLabel(mainController.getCurrentTime());
    }

    @FXML
    void oneDayButtonActionListener() {
        mainController.setDateString1Day(isForward);
        setTimeLabel(mainController.getCurrentTime());
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


    public void setTimeLabel(String time) {
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
