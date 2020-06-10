package controllers;

import Manager.TransPoolManager;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AppController {
    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private TripRequestController tripRequestController;

    @FXML
    private TripSuggestController tripSuggestController;

    private TransPoolManager transPoolManager;


    @FXML
    public void initialize() {
        if (headerComponentController != null && tripRequestController != null && tripSuggestController != null) {
            headerComponentController.setMainController(this);
            tripRequestController.setMainController(this);
            tripSuggestController.setMainController(this);
        }
        transPoolManager.getTransPoolManagerInstance();
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
        Platform.exit();
        System.exit(0);
    }

    public void loadXMLAction() {
        headerComponentController.loadXMLButtonActionListener();
    }

    public void addTripRequestAction(String[] inputTripRequestString) {
        StringBuilder errors = new StringBuilder();
        TripRequest newRequest = null;
        try {
            boolean isValidInput = this.transPoolManager.getEngineManager().validateTripRequestInput(inputTripRequestString);
            if (isValidInput) {
                newRequest = this.transPoolManager.addNewTripRequestSuccess(inputTripRequestString);
                tripRequestController.addNewTripRequestLabel(newRequest);
                tripRequestController.closeAddNewTripRequestStage();
            } else {
                errors = new StringBuilder(this.transPoolManager.getAddNewTripRequestErrorMessage());
                this.getAlertErrorWindow(errors.toString());
            }
        }
         catch (Exception e) {
                errors.append(e.getMessage());
                errors.append(this.transPoolManager.getAddNewTripRequestErrorMessage());
                this.getAlertErrorWindow(errors.toString());
            }
        //tripRequestController.addTripRequestButtonActionListener();
    }

    public void addTripSuggestAction(String[] inputTripSuggestString) {
        StringBuilder errors = new StringBuilder();
        TripSuggest newSuggest = null;
        try {
            HashSet<String> allStationsLogicNames = this.transPoolManager.getEngineManager().getAllLogicStationsName();
            boolean isValidInput = this.transPoolManager.getEngineManager()
                    .validateTripSuggestInput(inputTripSuggestString,allStationsLogicNames);
            if (isValidInput) {
                newSuggest = this.transPoolManager.addNewTripSuggestSuccess(inputTripSuggestString);
                tripSuggestController.addNewTripSuggestLabel(newSuggest);
                tripSuggestController.closeAddNewTripSuggestStage();
            } else {
                errors = new StringBuilder(this.transPoolManager.getAddNewTripSuggestErrorMessage());
                this.getAlertErrorWindow(errors.toString());
            }
        }
        catch (Exception e) {
            errors.append(e.getMessage());
            errors.append(this.transPoolManager.getAddNewTripRequestErrorMessage());
            this.getAlertErrorWindow(errors.toString());
        }
        //tripRequestController.addTripRequestButtonActionListener();
    }

    public List<String> CheckPathForXML(String myPathToTheXMLFile) {
            List<String> xmlErrors = new ArrayList<>();
            try {
                xmlErrors = this.transPoolManager.getEngineManager().LoadXML(myPathToTheXMLFile, xmlErrors);
                if(xmlErrors.isEmpty()) {
                    this.transPoolManager.setIsXMLLoaded(true);
                    transPoolManager = transPoolManager.getTransPoolManagerInstance();
                }
            } catch (Exception e) {
                xmlErrors.add(e.getMessage());
            } finally {
                //checkIfErrorsOccurredAndPrint(xmlErrors);
            }
            return xmlErrors;
    }

    public boolean isXMLLoaded() {
       return this.transPoolManager.isXMLLoaded();
    }

    public String getAllStationsNames() {
      return  this.transPoolManager.getEngineManager().getAllStationsName();
    }

    public void getAlertErrorWindow(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, message);
        errorAlert.showAndWait();
    }
}
