package Manager;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewWrapper {

    static BorderPane root = null;
    Stage primaryStage = null;

    private ViewWrapper() {
    }

    /** thread safe */
    public static ViewWrapper getInstance()
    {
        return SingletonHelper.instance;
    }

    private static class SingletonHelper {
        private static ViewWrapper instance = new ViewWrapper();
    }

    public static BorderPane getRoot() {
        return root;
    }

    public static void setRoot(BorderPane root) {
        ViewWrapper.root = root;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
