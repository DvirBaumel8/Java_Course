package controllers;

import Manager.EngineManager;
import Manager.TransPoolManager;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import com.fxgraph.graph.Graph;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

import static RootWrapper.RootWrapper.getGraph;
import static RootWrapper.RootWrapper.setGraph;

public class AppController {
    @FXML private ScrollPane headerComponent;

    @FXML private HeaderController headerComponentController;

    @FXML private TripRequestController tripRequestController;

    @FXML private TripSuggestController tripSuggestController;

    @FXML private MatchingController matchingController;

    @FXML private LiveMapController liveMapController;


    private TransPoolManager transPoolManager;

    private EngineManager engine;


    @FXML
    public void initialize() {
        if (headerComponentController != null && tripRequestController != null && tripSuggestController != null) {
            headerComponentController.setMainController(this);
            tripRequestController.setMainController(this);
            tripSuggestController.setMainController(this);
            matchingController.setMainController(this);
            liveMapController.setMainController(this);
        }
        transPoolManager = transPoolManager.getTransPoolManagerInstance();
        engine = transPoolManager.getEngineManager();
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

    //-------------------------------Exit Functions Main Controller -------------------------------------------
    public void exitButtonAction() {
        Platform.exit();
        System.exit(0);
    }



    //-------------------------------XML Functions Main Controller -------------------------------------------
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
                engine = transPoolManager.getEngineManager();
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



    //-------------------------------TripRequest Functions Main Controller---------------------------------------
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



    //-------------------------------TripSuggest Functions Main Controller---------------------------------------
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

    public Map<Integer, TripSuggest> getTripSuggestMap() {
        return this.transPoolManager.getEngineManager().getTripSuggestUtil().getAllSuggestedTrips();
    }

    public void resetTripSuggestAccordion() {
        tripSuggestController.resetTripSuggestAccordion();
    }



    //--------------------------------Matching Functions Main Controller -----------------------------------------
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

    public String matchTripRequestObject(String input, String requestIDAndAmountToMatch) {
        return engine.matchTripRequest(input, requestIDAndAmountToMatch);
    }

    public void setNeededTripRequestForMatchAccordion(String requestId) {
        tripRequestController.setNeededTripRequestForMatchAccordion(requestId);
    }

     public List<String> getAllUnmatchedRequests() {
       return engine.getAllUnmatchedRequests();
   }


    //--------------------------------Time Functions Main Controller -----------------------------------------
    public void setTime() {
        String timeStr = engine.getCurrentSystemTime().toString();
        headerComponentController.setTimeLabel(timeStr);
    }

    public void setDateString5Min(boolean isForward) {
        moveTime(isForward, 1);
        List<String> currentSuggestTripsDto = engine.getListDetailsPerTime();
        matchingController.updateVBoxForCurrentSuggestTripDisplay(currentSuggestTripsDto);
    }

    public void setDateString30Min(boolean isForward) {
        moveTime(isForward, 2);
        List<String> currentSuggestTripsDto = engine.getListDetailsPerTime();
        matchingController.updateVBoxForCurrentSuggestTripDisplay(currentSuggestTripsDto);
    }

    public void setDateString1Hour(boolean isForward) {
        moveTime(isForward, 3);
        List<String> currentSuggestTripsDto = engine.getListDetailsPerTime();
        matchingController.updateVBoxForCurrentSuggestTripDisplay(currentSuggestTripsDto);
    }

    public void setDateString2Hours(boolean isForward) {
        moveTime(isForward, 4);
        List<String> currentSuggestTripsDto = engine.getListDetailsPerTime();
        matchingController.updateVBoxForCurrentSuggestTripDisplay(currentSuggestTripsDto);
    }

    public void setDateString1Day(boolean isForward) {
        moveTime(isForward, 5);
        List<String> currentSuggestTripsDto = engine.getListDetailsPerTime();
        matchingController.updateVBoxForCurrentSuggestTripDisplay(currentSuggestTripsDto);
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



    //--------------------------------Ranking Functions Main Controller -----------------------------------------
    public List<String> validateRequestIdForRank(String requestId) {
        return engine.validateRequestIDExistInMatchedRequestTrip(requestId);
    }

    public List<String> getAllMatchingTripRequestForRank() {
        return engine.getAllMatchedTripRequest();
    }

    public List<String> getTripSuggestIdsFromTripRequestWhichNotRankYet(String requestId) {
        return engine.getTripSuggestIdsFromTripRequestWhichNotRankYet(requestId);
    }

    public List<String> validateInputOfRatingDriverOfSuggestIDAndRating(String tripSuggestId, String rank,
                                                                        String review) {
        return engine.validateInputOfRatingDriverOfSuggestIDAndRating(tripSuggestId, rank, review);
    }

    public void rankDriver(String[] inputs) {
        engine.rankDriver(inputs);
    }

    public void addSuggestedTripsDto(String[] displayTripSuggestStrArr) {
        engine.getTripSuggestUtil().addSuggestedTripsDto(displayTripSuggestStrArr);
    }

    public Map<String, List<String>> getSuggestedTripsMapDto() {
        return engine.getTripSuggestUtil().getSuggestedTripsDto();
    }

    public void setAverageInTripSuggestMapDto(String currRank, String suggestId) {
        engine.getTripSuggestUtil().setAverageInTripSuggestMapDto(currRank, suggestId);
    }

    public void setNumOfRanksInTripSuggestMapDto(String suggestId) {
        engine.getTripSuggestUtil().setNumOfRanksInTripSuggestMapDto(suggestId);
    }

    public void setAverageReviewsInTripSuggestMapDto(String newReview,  String suggestId) {
        engine.getTripSuggestUtil().setAverageReviewsInTripSuggestMapDto(newReview, suggestId);
    }

    public List<String>  getTripSuggestDtoObj(String suggestId) {
        return engine.getTripSuggestUtil().getTripSuggestDtoObj(suggestId);
    }



    //--------------------------------Live Map Functions Main Controller -----------------------------------------
    public void updateLiveMap(String currentTripSuggestButtonText) {
        Graph graphToUpdate = engine.getGraphBuilderUtil().setAndGetGraphByCurrentTripSuggest(currentTripSuggestButtonText);
        setGraph(graphToUpdate);
    }

    public void setLiveMapToRootCenter() {
        Graph graph = engine.getGraph();
        liveMapController = new LiveMapController();
        liveMapController.setLiveMapToRootCenter(graph);
    }


    //--------------------------------General Functions Main Controller -----------------------------------------
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

    public void resetSystem() {
    }
}
