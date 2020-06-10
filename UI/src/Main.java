import Manager.BorderPaneWrapper;
import Manager.ViewWrapper;
import XML.XMLLoading.jaxb.schema.generated.Route;

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
        ViewWrapper viewWrapper = ViewWrapper.getInstance();

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

        // load master app and controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.APP_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        viewWrapper.setRoot(fxmlLoader.load(url.openStream()));
        AppController appController = fxmlLoader.getController();

        // add sub components to master app placeholders
        viewWrapper.getRoot().setTop(headerComponent);
        viewWrapper.getRoot().setLeft(borderPaneTripRequest);
        viewWrapper.getRoot().setRight(borderPaneTripSuggest);

        appController.setHeaderComponentController(headerController);
        appController.setTripRequestComponentController(tripRequestController);
        appController.setTripSuggestComponentController(tripSuggestController);

        Scene scene = new Scene(viewWrapper.getRoot(), 700, 690);
        viewWrapper.setPrimaryStage(primaryStage);
        viewWrapper.getPrimaryStage().setScene(scene);
        viewWrapper.getPrimaryStage().show();
    }
}