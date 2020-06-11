import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

import Routes.*;
import controllers.*;

public class Main extends Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        CommonResourcesPaths route = CommonResourcesPaths.getInstance();

        // load header component and controller from fxml
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(route.HEADER_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane headerComponent = fxmlLoader.load(url.openStream());
        HeaderController headerController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.TRIP_REQUEST_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneTripRequest = fxmlLoader.load(url.openStream());
        TripRequestController tripRequestController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.TRIP_SUGGEST_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneTripSuggest = fxmlLoader.load(url.openStream());
        TripSuggestController tripSuggestController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.MATCHING_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneMatching = fxmlLoader.load(url.openStream());
        MatchingController matchingController = fxmlLoader.getController();

        //--------------------------------------------------------------------

        // load master app and controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.APP_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();

        // add sub components to master app placeholders
        root.setTop(headerComponent);
        root.setLeft(borderPaneTripRequest);
        root.setRight(borderPaneTripSuggest);
        root.setBottom(borderPaneMatching);

        appController.setHeaderComponentController(headerController);
        appController.setTripRequestComponentController(tripRequestController);
        appController.setTripSuggestComponentController(tripSuggestController);
        appController.setMatchingComponentController(matchingController);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}