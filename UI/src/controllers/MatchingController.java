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

    Stage matchingStage = null;
    TextField matchingTextField = null;

    @FXML
    Accordion matchingAccordion;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void matchingButtonActionListener() {
        if (mainController.isXMLLoaded()) {
            getMatchingWindow();
        }
        else {
            mainController.getAlertErrorWindow("XML doesnt load yet - please load one");
        }
    }

    public void getMatchingWindow()  {
        matchingStage = new Stage();
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

        matchingStage.setTitle("Matching Action");
        matchingStage.setScene(scene);
        matchingStage.show();
    }

    private void matchingLoadButtonAction(ActionEvent event) {
        String matchingInput = matchingTextField.getText();
        List<String> matchingErrors = new LinkedList<>();

            try {
                matchingErrors = mainController.matchingAction(matchingInput);
                if(matchingErrors.isEmpty()) {
                    String[] inputs = matchingInput.split(",");
                    this.updateMatchingAccordion(inputs[0], inputs[1]);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Matching Succeed");
                    successAlert.showAndWait();
                    matchingStage.close();
                }
                else {
                    throw new Exception();
                }
                }
            catch (Exception e){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Matching doesnt Succeed");
                   for(String error : matchingErrors) {
                    errorAlert.setContentText(error);
                    }
                    errorAlert.showAndWait();
        }
    }

    public void updateMatchingAccordion(String requestId, String suggestId) {
        String matchingTextArea = matchingAccordion.getPanes().get(0).getText();
        StringBuilder matchingTextAreaBuilder = new StringBuilder(matchingTextArea);
        matchingTextAreaBuilder.append(requestId + ',' + suggestId +  System.lineSeparator());
        TitledPane matchingTitledPane= new TitledPane("Matching Trip List", new TextArea(matchingTextAreaBuilder.toString()));
        matchingAccordion.getPanes().set(0,matchingTitledPane);
    }

    public Accordion getMatchingAccordion() {
        return matchingAccordion;
    }
}
