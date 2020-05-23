package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HeaderController {
    private AppController mainController;

    @FXML private Button exitButton;
    @FXML private Button loadXMLButton;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void exitButtonActionListener(ActionEvent event) {
        mainController.exitButtonAction();
    }

    @FXML
    void loadXMLButtonActionListener(ActionEvent event) {
        mainController.loadXMLAction();
    }
}
