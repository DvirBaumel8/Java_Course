package controllers;

import Manager.EngineManager;
import Manager.TransPoolManager;
import Time.Time;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class AppController {
    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private TripRequestController tripRequestController;

    @FXML
    private TripSuggestController tripSuggestController;

    @FXML
    private MatchingController matchingController;

    @FXML
    private LiveMapController liveMapController;

    private TransPoolManager transPoolManager;

    private EngineManager engine;


    @FXML
    public void initialize() {
        engine = EngineManager.getEngineManagerInstance();
        if (headerComponentController != null && tripRequestController != null && tripSuggestController != null) {
            headerComponentController.setMainController(this);
            tripRequestController.setMainController(this);
            tripSuggestController.setMainController(this);
            matchingController.setMainController(this);
            liveMapController.setMainController(this);
        }
        transPoolManager = transPoolManager.getTransPoolManagerInstance();
    }

    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setLiveMapComponentController(LiveMapController liveMapController) {
        this.liveMapController = liveMapController;
        liveMapController.setMainController(this);
    }

    public void setTripRequestComponentController(TripRequestController tripRequestController) {
        this.tripRequestController = tripRequestController;
        this.tripRequestController.setMainController(this);
    }

    public void setTripSuggestComponentController(TripSuggestController tripSuggestController) {
        this.tripSuggestController = tripSuggestController;
        this.tripSuggestController.setMainController(this);
    }

    public void setMatchingComponentController(MatchingController matchingController) {
        this.matchingController = matchingController;
        this.matchingController.setMainController(this);
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
            boolean isValidInput = engine.validateTripRequestInput(inputTripRequestString);
            if (isValidInput) {
                newRequest = this.transPoolManager.addNewTripRequestSuccess(inputTripRequestString);
                tripRequestController.addNewTripRequestAccordion(newRequest);
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
            HashSet<String> allStationsLogicNames = engine.getAllLogicStationsName();
            boolean isValidInput = engine.validateTripSuggestInput(inputTripSuggestString,allStationsLogicNames);
            if (isValidInput) {
                newSuggest = this.transPoolManager.addNewTripSuggestSuccess(inputTripSuggestString);
                tripSuggestController.addNewTripSuggestAccordion(newSuggest);
                tripSuggestController.closeAddNewTripSuggestStage();
            }
              else {
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
                xmlErrors = engine.LoadXML(myPathToTheXMLFile, xmlErrors);
                if(xmlErrors.isEmpty()) {
                    TransPoolManager.setIsXMLLoaded(true);
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

    public List<String> matchingAction(String inputMatchingString) {
        List<String> matchingErrors = new LinkedList<>();
        try {
            matchingErrors = engine.validateChooseRequestAndAmountOfSuggestedTripsInput(inputMatchingString);

            if (matchingErrors.isEmpty()) {
                matchingErrors = transPoolManager.matchTripRequestToTripSuggestActions(inputMatchingString);
            }
        }
        catch (Exception e) {
            matchingErrors.add(e.getMessage());
        }

        return matchingErrors;
    }

    public void setTime() {
       String timeStr = engine.getCurrentSystemTime().toString();
       liveMapController.setTimeLabel(timeStr);
    }

    public void setDateString5Min(boolean isForward) {
        engine.moveTimeForward(1);
    }

    public void setDateString30Min(boolean isForward) {
        engine.moveTimeForward(2);
    }

    public void setDateString1Hour(boolean isForward) {
        engine.moveTimeForward(3);
    }

    public void setDateString2Hours(boolean isForward) {
        engine.moveTimeForward(4);
    }
    public void setDateString1Day(boolean isForward) {
        engine.moveTimeForward(5);
    }

    public String getCurrentTime() {
        return engine.getCurrentSystemTime().toString();
    }
}
