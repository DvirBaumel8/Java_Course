package Manager;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BorderPaneWrapper {
    static BorderPane root = null;
    Stage primaryStage = null;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
}
