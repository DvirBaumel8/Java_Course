package controllers;

import Manager.TransPoolManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import java.util.ArrayList;
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
        boolean isValidInput = this.transPoolManager.getEngineManager().validateTripRequestInput(inputTripRequestString);

        if(!isValidInput) {
            try {
                this.transPoolManager.addNewTripRequestSuccess();
                if(isValidInput) {
                    this.transPoolManager.addNewTripRequestSuccess();
                }
                else {
                    errors = new StringBuilder(this.transPoolManager.getAddNewTripRequestErrorMessage());
                }
            }
            catch (Exception e) {
                errors.append(e.getMessage());
                errors.append(this.transPoolManager.getAddNewTripRequestErrorMessage());
            }
        }

        tripRequestController.addTripRequestButtonActionListener();
    }

    public void addTripSuggestAction() {
        tripSuggestController.addTripSuggestButtonActionListener();
    }

    public List<String> CheckPathForXML(String myPathToTheXMLFile) {
            List<String> xmlErrors = new ArrayList<>();
            try {
                xmlErrors = this.transPoolManager.getEngineManager().LoadXML(myPathToTheXMLFile, xmlErrors);
                if(xmlErrors.isEmpty()) {
                    this.transPoolManager.setIsXMLLoaded(true);
                }
            } catch (Exception e) {
                xmlErrors.add(e.getMessage());
            } finally {
                //checkIfErrorsOccurredAndPrint(xmlErrors);
            }
            return xmlErrors;
    }

    public String getAllStationsNames() {
      return  this.transPoolManager.getEngineManager().getAllStationsName();
    }
}
