import LiveMap.graph.component.coordinate.CoordinateNode;
import LiveMap.graph.component.coordinate.CoordinatesManager;
import LiveMap.graph.component.details.StationDetailsDTO;
import LiveMap.graph.component.road.ArrowEdge;
import LiveMap.graph.component.station.StationManager;
import LiveMap.graph.component.station.StationNode;
import LiveMap.graph.layout.MapGridLayout;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Routes.*;
import controllers.*;

public class Main extends Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph graphMap = new Graph();
        createMap(graphMap);

        CommonResourcesPaths route = CommonResourcesPaths.getInstance();

        // load header component and controller from fxml
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(route.HEADER_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane headerComponent = fxmlLoader.load(url.openStream());
        headerComponent.setFitToHeight(true);
        headerComponent.setFitToWidth(true);
        HeaderController headerController = fxmlLoader.getController();

        //--------------------------------------------------------------------

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.TRIP_REQUEST_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneTripRequest = fxmlLoader.load(url.openStream());
        borderPaneTripRequest.setFitToHeight(true);
        borderPaneTripRequest.setFitToWidth(true);
        TripRequestController tripRequestController = fxmlLoader.getController();

        //--------------------------------------------------------------------

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.TRIP_SUGGEST_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneTripSuggest = fxmlLoader.load(url.openStream());
        borderPaneTripSuggest.setFitToHeight(true);
        borderPaneTripSuggest.setFitToWidth(true);
        TripSuggestController tripSuggestController = fxmlLoader.getController();

        //--------------------------------------------------------------------

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.MATCHING_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneMatching = fxmlLoader.load(url.openStream());
        borderPaneMatching.setFitToHeight(true);
        borderPaneMatching.setFitToWidth(true);
        MatchingController matchingController = fxmlLoader.getController();

        //--------------------------------------------------------------------

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(route.LIVEMAP_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane borderPaneLiveMap = fxmlLoader.load(url.openStream());
        borderPaneLiveMap.setContent(graphMap.getCanvas());
        borderPaneLiveMap.setFitToHeight(true);
        borderPaneLiveMap.setFitToWidth(true);
        LiveMapController liveMapController = fxmlLoader.getController();

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
        root.setCenter(borderPaneLiveMap);

        appController.setHeaderComponentController(headerController);
        appController.setTripRequestComponentController(tripRequestController);
        appController.setTripSuggestComponentController(tripSuggestController);
        appController.setMatchingComponentController(matchingController);
        appController.setLiveMapComponentController(liveMapController);

        Scene scene = new Scene(root, 1400, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMap(Graph graph) {
        final Model model = graph.getModel();
        graph.beginUpdate();

        StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        createEdges(model, cm);

        graph.endUpdate();

        graph.layout(new MapGridLayout(cm, sm));
    }

    private void createEdges(Model model, CoordinatesManager cm) {
        ArrowEdge e13 = new ArrowEdge(cm.getOrCreate(2,2), cm.getOrCreate(7,9));
        e13.textProperty().set("L: 7 ; FC: 4");
        model.addEdge(e13); // 1-3

        ArrowEdge e34 = new ArrowEdge(cm.getOrCreate(7,9), cm.getOrCreate(4,6));
        e34.textProperty().set("L: 12 ; FC: 14");
        model.addEdge(e34); // 3-4

        ArrowEdge e23 = new ArrowEdge(cm.getOrCreate(5,5), cm.getOrCreate(7,9));
        e23.textProperty().set("L: 4 ; FC: 10");
        model.addEdge(e23); // 2-3

        Platform.runLater(() -> {
            e13.getLine().getStyleClass().add("line1");
            e13.getText().getStyleClass().add("edge-text");

            e34.getLine().getStyleClass().add("line2");
            e34.getText().getStyleClass().add("edge-text");

            e23.getLine().getStyleClass().add("line3");
            e23.getText().getStyleClass().add("edge-text");

            //moveAllEdgesToTheFront(graph);
        });
    }

    private CoordinatesManager createCoordinates(Model model) {
        CoordinatesManager cm = new CoordinatesManager(CoordinateNode::new);

        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                model.addCell(cm.getOrCreate(i+1, j+1));
            }
        }

        return cm;
    }

    private StationManager createStations(Model model) {
        StationManager sm = new StationManager(StationNode::new);

        StationNode station = sm.getOrCreate(2, 2);
        station.setName("This is a test for long string");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            trips.add("Mosh");
            trips.add("Menash");
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        station = sm.getOrCreate(5, 5);
        station.setName("B");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        station = sm.getOrCreate(7, 9);
        station.setName("C");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            trips.add("Mosh");
            trips.add("Menash");
            trips.add("Tikva");
            trips.add("Mazal");
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        station = sm.getOrCreate(4, 6);
        station.setName("D");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            trips.add("Mazal");
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        return sm;
    }

    private void moveAllEdgesToTheFront(Graph graph) {

        List<Node> onlyEdges = new ArrayList<>();

        // finds all edge nodes and remove them from the beginning of list
        ObservableList<Node> nodes = graph.getCanvas().getChildren();
        while (nodes.get(0).getClass().getSimpleName().equals("EdgeGraphic")) {
            onlyEdges.add(nodes.remove(0));
        }

        // adds them as last ones
        nodes.addAll(onlyEdges);
    }
}