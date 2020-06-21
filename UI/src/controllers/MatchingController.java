package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
    public void addHBoxCellForCurrentSuggestTripDisplay() {
        addHBoxCell();
    }

    public void addHBoxCell() {
        //BorderPane layout = new BorderPane();

       // List<HBoxCell> list = new ArrayList<>();
        //for (int i = 0; i < 12; i++) {
          //  list.add(new HBoxCell("Item " + i, "Button " + i));
        //}

      //  ListView<HBoxCell> listView = new ListView<HBoxCell>();

        //Button button1 = new Button("Click");
        //currSuggestTripsVBox.getChildren().add(button1);
        //Button button2 = new Button("Hey");
        //currSuggestTripsVBox.getChildren().add(button2);

    }
}
