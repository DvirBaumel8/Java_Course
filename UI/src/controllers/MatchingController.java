package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MatchingController {

    private AppController mainController;

    @FXML private Button matchingButton;

    Stage mainMatchingStage = null;
    TextField matchingTextField = null;
    boolean isMainStageSucc = false;

    Stage potentialSuggestedTripsToMatchStage = null;
    TextField suggestedTripsToMatchTextField = null;

    @FXML private VBox currSuggestTripsVBox;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    //-------------------------------Main Window Match Flow UI---------------------------------------
    @FXML
    void matchingButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            getMainMatchingWindow();
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    public void getMainMatchingWindow()  {
        mainMatchingStage = new Stage();
        VBox matchingWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(10,10,10,10);
        matchingWindow.setSpacing(10);
        List<String> unMatchedRequests = mainController.getAllUnmatchedRequests();
        String unMatchedRequestsStringToPrint = unMatchedRequests.toString();
        if(unMatchedRequests.isEmpty()) {
            unMatchedRequestsStringToPrint = "No potential un matched requests";
            this.isMainStageSucc = false;
        }
        else {
            this.isMainStageSucc = true;
            unMatchedRequestsStringToPrint = unMatchedRequestsStringToPrint.replace("[","");
            unMatchedRequestsStringToPrint = unMatchedRequestsStringToPrint.replace("]","");
        }


        ScrollPane scrollMainMatchingWindow = new ScrollPane();

        Label matchingLabel = new Label("Here is all the Unmatched Requests To Match:" + System.lineSeparator() +
                "Please Choose one of the following index to match" + System.lineSeparator() + System.lineSeparator()
                + unMatchedRequestsStringToPrint + System.lineSeparator() + System.lineSeparator() +
                "In the following way:" + System.lineSeparator() +
                "Separated numbers with , :" + System.lineSeparator() +
                "Request ID, Maximum suggested trips you want to be display" + System.lineSeparator() +
                "For example:" + System.lineSeparator() +
                "1,4" + System.lineSeparator() +
                "(1 - request trip id, 4 - Maximum of amount of suggested trips)");
        matchingLabel.setPrefWidth(400);

        matchingWindow.getChildren().add(matchingLabel);

        matchingTextField = new TextField("1,4");
        matchingTextField.setPrefWidth(120);
        matchingTextField.setMaxWidth(120);
        matchingWindow.getChildren().add(matchingTextField);

        Button matchingLoadButton= new Button("Match");
        matchingLoadButton.setPrefWidth(100);
        matchingLoadButton.setTranslateX(165);
        matchingLoadButton.setOnAction(this::matchingLoadButtonAction);
        matchingWindow.getChildren().add(matchingLoadButton);

        scrollMainMatchingWindow.setPrefWidth(450);
        scrollMainMatchingWindow.setContent(matchingWindow);

        matchingWindow.setMargin(matchingLabel, margin);
        matchingWindow.setMargin(matchingTextField, margin);
        matchingWindow.setMargin(matchingLoadButton, margin);

        Scene scene = new Scene(matchingWindow, 450, 300);

        mainMatchingStage.setTitle("Matching Action");
        mainMatchingStage.setScene(scene);
        mainMatchingStage.show();
    }

    private void matchingLoadButtonAction(ActionEvent event) {
            try {
                if (this.isMainStageSucc) {
                    mainMatchingStage.close();
                    getPotentialSuggestedTripsToMatchWindow();
                }
                else {
                    Alert noRequestAlert = new Alert(Alert.AlertType.ERROR, "Matching doesnt Succeed - no requests");
                    noRequestAlert.showAndWait();
                }
                }
            catch (Exception e){
              Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Matching doesnt Succeed" + e.getMessage());
              errorAlert.showAndWait();
        }
    }



    //-------------------------------Second Window Match Flow UI---------------------------------------
    public void getPotentialSuggestedTripsToMatchWindow()  {
        potentialSuggestedTripsToMatchStage = new Stage();
        VBox potentialSuggestedTripsToMatchWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(10,10,10,10);

        ScrollPane scrollPanePotentialSuggestedTripsToMatch = new ScrollPane();
        List<String> potentialSuggestedTripsToMatch = mainController.getPotentialSuggestedTripsToMatch(matchingTextField.getText());
        String potentialSuggestedTripsToMatchStringToPrint = potentialSuggestedTripsToMatch.toString();
        if(potentialSuggestedTripsToMatchStringToPrint.length() < 4) {
            potentialSuggestedTripsToMatchStringToPrint = "***No potential suggested trips to match***";
        }
        else {
            potentialSuggestedTripsToMatchStringToPrint = potentialSuggestedTripsToMatchStringToPrint.replace("[","");
            potentialSuggestedTripsToMatchStringToPrint = potentialSuggestedTripsToMatchStringToPrint.replace("]","");
        }

        Label potentialSuggestedIdsToMatchLabel = new Label("Choose index to match from the following options:" + System.lineSeparator() +
                 System.lineSeparator() + potentialSuggestedTripsToMatchStringToPrint +
                System.lineSeparator() + System.lineSeparator() +
                "insert here the desired index for match:" + System.lineSeparator() + System.lineSeparator());

        potentialSuggestedTripsToMatchWindow.getChildren().add(potentialSuggestedIdsToMatchLabel);
        suggestedTripsToMatchTextField = null;
        suggestedTripsToMatchTextField = new TextField();
        suggestedTripsToMatchTextField.setText("1");
        suggestedTripsToMatchTextField.setPrefWidth(120);
        suggestedTripsToMatchTextField.setMaxWidth(120);
        potentialSuggestedTripsToMatchWindow.getChildren().add(matchingTextField);

        Button suggestedTripsToMatchButton= new Button("Match");
        suggestedTripsToMatchButton.setPrefWidth(100);
        suggestedTripsToMatchButton.setTranslateX(160);
        suggestedTripsToMatchButton.setOnAction(this::suggestedTripsToMatchButtonAction);
        potentialSuggestedTripsToMatchWindow.getChildren().add(suggestedTripsToMatchButton);

        potentialSuggestedTripsToMatchWindow.setMargin(potentialSuggestedIdsToMatchLabel, margin);
        potentialSuggestedTripsToMatchWindow.setMargin(suggestedTripsToMatchButton, margin);

        scrollPanePotentialSuggestedTripsToMatch.setContent(potentialSuggestedTripsToMatchWindow);

        Scene scene = new Scene(scrollPanePotentialSuggestedTripsToMatch, 450, 350);
        potentialSuggestedTripsToMatchStage.setTitle("Matching Action - potential suggested Ids to match");
        potentialSuggestedTripsToMatchStage.setScene(scene);
        potentialSuggestedTripsToMatchStage.show();
    }

    public void suggestedTripsToMatchButtonAction(ActionEvent event) {
        try {
            potentialSuggestedTripsToMatchStage.close();
            String roadTrip = mainController.
                    matchTripRequestObject(suggestedTripsToMatchTextField.getText(),matchingTextField.getText());
            if(!roadTrip.isEmpty()) {
                mainController.setNeededTripRequestForMatchAccordion(matchingTextField.getText());
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Matching Succeed");
                successAlert.showAndWait();
            }
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Matching doesnt Succeed");
            errorAlert.showAndWait();
        }
        potentialSuggestedTripsToMatchStage.close();
    }



    //-------------------------------Current Suggest Trips HBox UI---------------------------------------

    public void updateVBoxForCurrentSuggestTripDisplay(List<String> currentSuggestTripsDto) {
        resetVBoxForCurrentSuggestTripDisplay();
        currentSuggestTripsDto.forEach((currentSuggestTrip) -> {
            String[] inputs = currentSuggestTrip.split(",");
            String suggestName = inputs[0];
            String suggestId = inputs[1];;
            String currStation = inputs[2];
            addCellToVBoxForCurrentSuggestTripDisplay(suggestName, suggestId, currStation);
        });
    }

    public void addCellToVBoxForCurrentSuggestTripDisplay(String suggestName, String suggestId, String currStation) {
        String currentSuggestTripDisplay = suggestName + " , " + suggestId + " , " + currStation;
        Button newSuggestTripDisplayButton = new Button(currentSuggestTripDisplay;
        newSuggestTripDisplayButton.setPrefWidth(250);
        newSuggestTripDisplayButton.setMinWidth(250);
        newSuggestTripDisplayButton.setPrefHeight(20);
        newSuggestTripDisplayButton.setMaxHeight(20);
        newSuggestTripDisplayButton.setStyle("-fx-font-size:10");
        currSuggestTripsVBox.getChildren().add(newSuggestTripDisplayButton);
    }

    void resetVBoxForCurrentSuggestTripDisplay() {
        int sizeOfCurrentPanes = currSuggestTripsVBox.getChildren().size();

        for(int i = 0 ; i < sizeOfCurrentPanes ; i++) {
            this.currSuggestTripsVBox.getChildren().remove(i);
        }
    }

    public void addHBoxCell() {
        //BorderPane layout = new BorderPane();


        // List<HBoxCell> list = new ArrayList<>();
        //for (int i = 0; i < 12; i++) {
          //  list.add(new HBoxCell("Item " + i, "Button " + i));
        //}

      //  ListView<HBoxCell> listView = new ListView<HBoxCell>();

        Button button1 = new Button("Click");
        button1.setPrefWidth(250);
        button1.setMinWidth(250);
        button1.setPrefHeight(20);
        button1.setMaxHeight(20);
        button1.setStyle("-fx-font-size:10");
        currSuggestTripsVBox.getChildren().add(button1);
        Button button2 = new Button("Click");
        button2.setPrefWidth(250);
        button2.setMinWidth(250);
        button2.setPrefHeight(20);
        button2.setMaxHeight(20);
        button2.setStyle("-fx-font-size:10");
        Button button3 = new Button("Click");
        button3.setPrefWidth(250);
        button3.setMinWidth(250);
        button3.setPrefHeight(20);
        button3.setMaxHeight(20);
        button3.setStyle("-fx-font-size:10");
        Button button4 = new Button("Click");
        button4.setPrefWidth(250);
        button4.setMinWidth(250);
        button4.setPrefHeight(20);
        button4.setMaxHeight(20);
        button4.setStyle("-fx-font-size:10");
        Button button5 = new Button("Click");
        button5.setPrefWidth(250);
        button5.setMinWidth(250);
        button5.setPrefHeight(20);
        button5.setMaxHeight(20);
        button5.setStyle("-fx-font-size:10");
        Button button6 = new Button("Click");
        button6.setPrefWidth(250);
        button6.setMinWidth(250);
        button6.setPrefHeight(20);
        button6.setMaxHeight(20);
        button6.setStyle("-fx-font-size:10");
        Button button7 = new Button("Click");
        button7.setPrefWidth(250);
        button7.setMinWidth(250);
        button7.setPrefHeight(20);
        button7.setMaxHeight(20);
        button7.setStyle("-fx-font-size:10");
        Button button8 = new Button("Click");
        button8.setPrefWidth(250);
        button8.setMinWidth(250);
        button8.setPrefHeight(20);
        button8.setMaxHeight(20);
        button8.setStyle("-fx-font-size:10");
        Button button9 = new Button("Click");
        button9.setPrefWidth(250);
        button9.setMinWidth(250);
        button9.setPrefHeight(20);
        button9.setMaxHeight(20);
        button9.setStyle("-fx-font-size:10");
        Button button10 = new Button("Click");
        button10.setPrefWidth(250);
        button10.setMinWidth(250);
        button10.setPrefHeight(20);
        button10.setMaxHeight(20);
        button10.setStyle("-fx-font-size:10");
        Button button11 = new Button("Click");
        button11.setPrefWidth(250);
        button11.setMinWidth(250);
        button11.setPrefHeight(20);
        button11.setMaxHeight(20);
        button11.setStyle("-fx-font-size:10");
        currSuggestTripsVBox.getChildren().add(button3);
        currSuggestTripsVBox.getChildren().add(button4);
        currSuggestTripsVBox.getChildren().add(button5);
        currSuggestTripsVBox.getChildren().add(button6);
        currSuggestTripsVBox.getChildren().add(button7);
        currSuggestTripsVBox.getChildren().add(button8);
        currSuggestTripsVBox.getChildren().add(button9);
        currSuggestTripsVBox.getChildren().add(button10);



    }
}
