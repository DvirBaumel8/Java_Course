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
import java.util.List;

public class HeaderController {
    private AppController mainController;

    @FXML private Button exitButton;

    @FXML private Button loadXMLButton;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    TextField pathTextField = new TextField("full path to .xml");
    Stage xmlStage = new Stage();


    @FXML
    void exitButtonActionListener() {
        mainController.exitButtonAction();
    }

    @FXML
    void loadXMLButtonActionListener() {
       getXMLWindow();
    }

    private void xmlHandleButtonAction(ActionEvent event) {
        String userFullPath = pathTextField.getText();
        List<String> xmlErrors = mainController.CheckPathForXML(userFullPath);
        if(xmlErrors.isEmpty()) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "XML Load Successfully");
                successAlert.showAndWait();
                xmlStage.close();
        }
        else{
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

        pathTextField = new TextField("full path to .xml");
        pathTextField.setPrefWidth(120);
        xmlWindow.getChildren().add(pathTextField);

        Button xmlLoadButton= new Button("Load");
        xmlLoadButton.setOnAction(this::xmlHandleButtonAction);
        xmlWindow.getChildren().add(xmlLoadButton);

        xmlWindow.setMargin(xmlLabel, margin);
        xmlWindow.setMargin(pathTextField, margin);
        xmlWindow.setMargin(xmlLoadButton, margin);

        Scene scene = new Scene(xmlWindow, 300, 200);

        xmlStage.setTitle("XML Loading");
        xmlStage.setScene(scene);
        xmlStage.show();
    }
}
