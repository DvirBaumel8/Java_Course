package controllers;

import MatchingUtil.RoadTrip;
import Manager.EngineManager;
import Manager.TransPoolManager;
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
        List<String> errorList = new LinkedList<>();
        TripRequest newRequest = null;
        try {
            boolean isValidInput = engine.validateTripRequestInput(inputTripRequestString);
            if (isValidInput) {
                newRequest = this.transPoolManager.addNewTripRequestSuccess(inputTripRequestString);
                tripRequestController.addNewTripRequestAccordion(newRequest);
                tripRequestController.closeAddNewTripRequestStage();
            } else {
                errors = new StringBuilder(this.transPoolManager.getAddNewTripRequestErrorMessage());
                errorList.add(errors.toString());
                this.getAlertErrorWindow(errorList);
            }
        }
         catch (Exception e) {
                errors.append(e.getMessage());
                errors.append(this.transPoolManager.getAddNewTripRequestErrorMessage());
                errorList.add(errors.toString());
                this.getAlertErrorWindow(errorList);
            }
        //tripRequestController.addTripRequestButtonActionListener();
    }

    public void addTripSuggestAction(String[] inputTripSuggestString) {
        StringBuilder errors = new StringBuilder();
        List<String> errorsList = new LinkedList<>();
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
                errorsList.add(errors.toString());
                this.getAlertErrorWindow(errorsList);
            }
        }
        catch (Exception e) {
            errors = new StringBuilder(this.transPoolManager.getAddNewTripSuggestErrorMessage());
            errorsList.add(errors.toString());
            this.getAlertErrorWindow(errorsList);
        }
        //tripRequestController.addTripRequestButtonActionListener();
    }

    public Map<TripSuggest, Integer> getTripSuggestMap() {
        return this.transPoolManager.getEngineManager().getTripSuggestUtil().getAllSuggestedTrips();
    }


    public void loadInitTripSuggestFromXML() {
        tripSuggestController.loadTripSuggestFromXML();
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

    public void getAlertErrorWindow(List<String> message) {
        StringBuilder stringBuilder = new StringBuilder();
        message.forEach((mess) -> {
            stringBuilder.append(mess + System.lineSeparator());
        });
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, stringBuilder.toString());
        errorAlert.showAndWait();
    }

    public void getSuccessWindow(String message) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, message);
        successAlert.showAndWait();
    }

<<<<<<< HEAD
=======
    public List<String> matchingAction(String inputMatchingString) {
        List<String> validationsErrors = new LinkedList<>();
        try {
            validationsErrors = engine.validateChooseRequestAndAmountOfSuggestedTripsInput(inputMatchingString);

            if (validationsErrors.isEmpty()) {
                List<String> roadTrips = engine.findPotentialSuggestedTripsToMatch(inputMatchingString);
                //Ohad - Todo display the suggested road trips to user + Total trip cost, arrival/start time (depend on the user choice) average fuel amount in the road.
                String userPotentialSuggestChoice = "1"; //TODO
                boolean response = engine.matchTripRequest(userPotentialSuggestChoice, inputMatchingString);
                //TODo - Success message
            }
        }
        catch (Exception e) {
            validationsErrors.add(e.getMessage());
        }

        return validationsErrors;
    }

>>>>>>> c55c93535e1eb68c81dc6d72905260a7553069d7
    public void setTime() {
       String timeStr = engine.getCurrentSystemTime().toString();
       liveMapController.setTimeLabel(timeStr);
    }

    public void setDateString5Min(boolean isForward) {
        moveTime(isForward, 1);
    }

    public void setDateString30Min(boolean isForward) {
        moveTime(isForward, 2);
    }

    public void setDateString1Hour(boolean isForward) {
        moveTime(isForward, 3);
    }

    public void setDateString2Hours(boolean isForward) {
        moveTime(isForward, 4);
    }

    public void setDateString1Day(boolean isForward) {
        moveTime(isForward, 5);
    }

    private void moveTime(boolean forward, int choose) {
        if(forward) {
            engine.moveTimeForward(choose);
        }
        else {
            try {
                engine.moveTimeBack(choose);
            }
            catch (Exception ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Time can't be lower then day 1 at 00:00");
                errorAlert.setContentText(ex.getMessage());
                errorAlert.showAndWait();
            }

        }
    }

    public String getCurrentTime() {
        return engine.getCurrentSystemTime().toString();
    }

    public Accordion getMatchingAccordion() {
        return matchingController.getMatchingAccordion();
    }

    public List<String> validateRequestIdForRank(String requestId) {
        return engine.validateRequestIDExistInMatchedRequestTrip(requestId);
    }

    public boolean validateSuggestIdForRank(String suggestId) {

        return true;
    }

    public List<String> getAllMatchingTripRequestForRank() {
        return engine.getAllMatchedTripRequest();
    }

    public List<String> getTripSuggestIdsFromTripRequestWhichNotRankYet(String requestId) {
        return engine.getTripSuggestIdsFromTripRequestWhichNotRankYet(requestId);
    }

    public List<String> validateInputOfRatingDriverOfSuggestIDAndRating(String requestId, String rank,
                                                                String review) {
        return engine.validateInputOfRatingDriverOfSuggestIDAndRating(requestId, rank, review);
    }

    public List<String> getPotentialSuggestedTripsToMatch(String inputMatchingString) {
        List<String> validationsErrors = new LinkedList<>();
        try {
            validationsErrors = engine.validateChooseRequestAndAmountOfSuggestedTripsInput(inputMatchingString);

            if (validationsErrors.isEmpty()) {
                return engine.findPotentialSuggestedTripsToMatch(inputMatchingString);
            }
        }
        catch (Exception e) {
            validationsErrors.add(e.getMessage());
        }
        return validationsErrors;
    }

    public String matchTripRequestObject(String secondInput, String firstInput) {
        return engine.matchTripRequest(secondInput, firstInput);
    }


    public void rankDriver(String input) {
        engine.rankDriver(input);
    }
}
