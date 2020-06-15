package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.List;

public class MatchingController {

    private AppController mainController;

    @FXML
    private Button matchingButton;

    @FXML
    Accordion matchingAccordion;

    Stage mainMatchingStage = null;
    TextField matchingTextField = null;

    Stage potentialSuggestedTripsToMatchStage = null;
    TextField suggestedTripsToMatchTextField = null;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

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

        Label matchingLabel = new Label("Match your trip request in the following way:" + System.lineSeparator() +
                "Separated numbers with , :" + System.lineSeparator() +
                "Request ID, Maximum suggested trips  you want to be display" + System.lineSeparator() +
                "For example:" + System.lineSeparator() +
                "1,4 (1 - request trip id, 4 - Maximum of amount of suggested trips)");
        matchingLabel.setPrefWidth(400);

        matchingWindow.getChildren().add(matchingLabel);

        matchingTextField = new TextField("1,4");
        matchingWindow.getChildren().add(matchingTextField);

        Button matchingLoadButton= new Button("Match");
        matchingTextField.setPrefWidth(100);
        matchingLoadButton.setTranslateX(165);
        matchingLoadButton.setOnAction(this::matchingLoadButtonAction);
        matchingWindow.getChildren().add(matchingLoadButton);

        matchingWindow.setMargin(matchingLabel, margin);
        matchingWindow.setMargin(matchingTextField, margin);
        matchingWindow.setMargin(matchingLoadButton, margin);

        Scene scene = new Scene(matchingWindow, 400, 200);

        mainMatchingStage.setTitle("Matching Action");
        mainMatchingStage.setScene(scene);
        mainMatchingStage.show();
    }

    private void matchingLoadButtonAction(ActionEvent event) {
        String matchingInput = matchingTextField.getText();
        List<String> potentialSuggestedTripsToMatch = new LinkedList<>();

            try {
                //potentialSuggestedTripsToMatch = mainController.getPotentialSuggestedTripsToMatch(matchingInput);
                //if(potentialSuggestedTripsToMatch.size() > 0) {
                    getPotentialSuggestedIdsToMatchWindow(potentialSuggestedTripsToMatch);
                    mainMatchingStage.close();
                    mainController.validateAndActionOfPotentialSuggestedTripsToMatch(suggestedTripsToMatchTextField.getText());
                //}
                String[] inputs = matchingInput.split(",");
                this.updateMatchingAccordion(inputs[0], inputs[1]);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Matching Succeed");
                successAlert.showAndWait();
                mainMatchingStage.close();

                }
            catch (Exception e){
              Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Matching doesnt Succeed");
              errorAlert.showAndWait();
        }
    }

    public void getPotentialSuggestedIdsToMatchWindow(List<String> potentialSuggestedTripsToMatch)  {
        potentialSuggestedTripsToMatchStage = new Stage();
        VBox potentialSuggestedTripsToMatchWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(10,10,10,10);
        potentialSuggestedTripsToMatchWindow.setSpacing(10);

        ScrollPane scrollPanePotentialSuggestedTripsToMatch = new ScrollPane();

        Label potentialSuggestedIdsToMatchLabel = new Label("Choose suggest id to match from the following options:" + System.lineSeparator() +
                 System.lineSeparator() + potentialSuggestedTripsToMatch.toString() +
                System.lineSeparator() + System.lineSeparator() +
                "insert here the desired suggest id for match:");
        potentialSuggestedIdsToMatchLabel.setPrefWidth(400);

        potentialSuggestedTripsToMatchWindow.getChildren().add(potentialSuggestedIdsToMatchLabel);

        suggestedTripsToMatchTextField = new TextField("1,4");
        potentialSuggestedTripsToMatchWindow.getChildren().add(matchingTextField);

        Button suggestedTripsToMatchButton= new Button("Match");
        suggestedTripsToMatchButton.setPrefWidth(100);
        suggestedTripsToMatchButton.setTranslateX(165);
        suggestedTripsToMatchButton.setOnAction(this::matchingLoadButtonAction);
        potentialSuggestedTripsToMatchWindow.getChildren().add(suggestedTripsToMatchButton);

        potentialSuggestedTripsToMatchWindow.setMargin(potentialSuggestedIdsToMatchLabel, margin);
        potentialSuggestedTripsToMatchWindow.setMargin(suggestedTripsToMatchTextField, margin);
        potentialSuggestedTripsToMatchWindow.setMargin(suggestedTripsToMatchButton, margin);

        scrollPanePotentialSuggestedTripsToMatch.setContent(potentialSuggestedTripsToMatchWindow);

        Scene scene = new Scene(scrollPanePotentialSuggestedTripsToMatch, 400, 300);

        mainMatchingStage.setTitle("Matching Action - potential suggested Ids to match");
        mainMatchingStage.setScene(scene);
        mainMatchingStage.show();
    }


    public void updateMatchingAccordion(String requestId, String suggestId) {
        String matchingTextArea = matchingAccordion.getPanes().get(0).getText();
        StringBuilder matchingTextAreaBuilder = new StringBuilder(matchingTextArea);
        matchingTextAreaBuilder.append(requestId + ',' + suggestId +  System.lineSeparator());
        TitledPane matchingTitledPane = new TitledPane("Matching Trip List", new TextArea(matchingTextAreaBuilder.toString()));
        matchingAccordion.getPanes().set(0,matchingTitledPane);
    }

    public Accordion getMatchingAccordion() {
        return matchingAccordion;
    }

    public String getOptionalSuggestIdsForMatch() {

    }
}
