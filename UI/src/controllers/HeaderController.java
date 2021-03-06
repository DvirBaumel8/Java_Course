package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class HeaderController {
    private AppController mainController;

    @FXML private Button exitButton;

    @FXML private Button loadXMLButton;

    Stage xmlStage = null;
    TextField xmlPathTextField = null;

    @FXML private Label timeLabel;

    @FXML private Button forwardPreviousButton;
    private boolean isForward = true;

    @FXML private Button fiveMinButton;

    @FXML private Button halfHourMinButton;

    @FXML private Button oneHourButton;

    @FXML private Button twoHoursButton;

    @FXML private Button oneDayButton;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public HeaderController() {
        this.xmlPathTextField = new TextField("full path to .xml");
    }


    //-------------------------------XML Flow UI---------------------------------------
    @FXML
    void loadXMLButtonActionListener() {
       getXMLWindow();
    }

    private void xmlHandleButtonAction(ActionEvent event) {
        String userFullPath = xmlPathTextField.getText();
        List<String> xmlErrors = mainController.CheckPathForXML(userFullPath);
        if(xmlErrors.isEmpty()) {
                mainController.resetTripSuggestAccordion();
                mainController.setLiveMapToRootCenter();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "XML Load Successfully");
                successAlert.showAndWait();
                xmlStage.close();
                mainController.loadInitTripSuggestFromXML();
                mainController.setTime();
                mainController.updateCurrentSuggestTripsOnFirstLoad();
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "XML doesnt Load Successfully");
            for(String error : xmlErrors) {
                errorAlert.setContentText(error);
            }
            errorAlert.showAndWait();
        }
    }

    public void getXMLWindow()  {
        xmlStage = new Stage();
        VBox xmlWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(10,10,10,10);
        xmlWindow.setSpacing(10);
        Label xmlLabel = new Label("Please copy your full path to .xml file:");
        xmlLabel.setPrefWidth(300);
        xmlWindow.getChildren().add(xmlLabel);

        xmlPathTextField = new TextField("full path to .xml");
        xmlPathTextField.setPrefWidth(120);
        xmlWindow.getChildren().add(xmlPathTextField);

        Button xmlLoadButton= new Button("Load");
        xmlLoadButton.setOnAction(this::xmlHandleButtonAction);
        xmlWindow.getChildren().add(xmlLoadButton);

        xmlWindow.setMargin(xmlLabel, margin);
        xmlWindow.setMargin(xmlPathTextField, margin);
        xmlWindow.setMargin(xmlLoadButton, margin);

        Scene scene = new Scene(xmlWindow, 300, 200);

        xmlStage.setTitle("XML Loading");
        xmlStage.setScene(scene);
        xmlStage.show();
    }


    //-------------------------------Time Flow UI---------------------------------------
    @FXML
    void fiveMinButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            mainController.setDateString5Min(isForward);
            setTimeLabel(mainController.getCurrentTime());
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    @FXML
    void halfHourMinButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            mainController.setDateString30Min(isForward);
            setTimeLabel(mainController.getCurrentTime());
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    @FXML
    void oneHourButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            mainController.setDateString1Hour(isForward);
            setTimeLabel(mainController.getCurrentTime());
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    @FXML
    void twoHoursButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            mainController.setDateString2Hours(isForward);
            setTimeLabel(mainController.getCurrentTime());
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    @FXML
    void oneDayButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            mainController.setDateString1Day(isForward);
            setTimeLabel(mainController.getCurrentTime());
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    @FXML
    void forwardPreviousButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            isForward = !isForward;
            if(isForward) {
                forwardPreviousButton.setText("Forward");
            }
            else {
                forwardPreviousButton.setText("Previous");
            }
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    public void setTimeLabel(String time) {
        timeLabel.setText("Time:"+time);
    }



    //-------------------------------Exit Flow UI---------------------------------------
    @FXML
    void exitButtonActionListener() {
        mainController.exitButtonAction();
    }


}
