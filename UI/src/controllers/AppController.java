package controllers;

import Manager.TransPoolManager;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

import javax.swing.*;
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

    public boolean loadXMLButtonAction() {
        boolean res = false;

        if(!this.transPoolManager.isXMLLoaded()) {
            List<String> xmlErrors = new ArrayList<>();
            try {
                String myPathToTheXMLFile = null;
                // do pop up and get string full path
                // System.out.println("Please copy your full path to master.xml file and than press enter:");
                xmlErrors = this.transPoolManager.getEngineManager().LoadXML(myPathToTheXMLFile, xmlErrors);
                if(xmlErrors == null) {
                    this.transPoolManager.setIsXMLLoaded(true);
                }
                else {
                    // xml faild popup message
                    //this.isXMLLoaded = this.transPoolManager.printXMLResultAction(xmlErrors);
                }
            } catch (Exception e) {
                xmlErrors.add(e.getMessage());
            } finally {
                //checkIfErrorsOccurredAndPrint(xmlErrors);
            }
        }

        return res;
    }
}
