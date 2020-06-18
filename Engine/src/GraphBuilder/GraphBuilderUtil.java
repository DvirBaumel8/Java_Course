package GraphBuilder;

import Time.Time;
import XML.XMLLoading.jaxb.schema.generated.Path;
import XML.XMLLoading.jaxb.schema.generated.Stop;
import XML.XMLLoading.jaxb.schema.generated.TransPool;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import graph.component.coordinate.CoordinateNode;
import graph.component.coordinate.CoordinatesManager;
import graph.component.details.StationDetailsDTO;
import graph.component.road.ArrowEdge;
import graph.component.station.StationManager;
import graph.component.station.StationNode;
import graph.layout.MapGridLayout;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class GraphBuilderUtil {
    private TransPool transPool;
//suggest to build the graph for the xml
    //edge , i get statuions in the xm,l and supposed to build it
    //do sytry and one
    //statios ok
    //edjs check again
    public GraphBuilderUtil(TransPool transPool) {
        this.transPool = transPool;
    }



    private void createEdges(Model model, CoordinatesManager cm) {
        ArrowEdge arrowEdge;
        List<Path> pathList = transPool.getMapDescriptor().getPaths().getPath();
        for(Path path : pathList) {
            String to = path.getTo();
            String from = path.getFrom();
            boolean isOneWay = path.isOneWay();
            for(CoordinateNode node : cm.getAllCoordinates()) {

            }
        }
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
    private void createEdges1(Model model, CoordinatesManager cm) {
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
        int mapLength = transPool.getMapDescriptor().getMapBoundries().getLength();
        int mapWidth = transPool.getMapDescriptor().getMapBoundries().getWidth();

        for (int i=0; i<mapLength; i++) {
            for (int j = 0; j < mapWidth; j++) {
                model.addCell(cm.getOrCreate(i+1, j+1));
            }
        }

        return cm;
    }
    private CoordinatesManager createCoordinates1(Model model) {
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
        StationNode node;

        for(Stop station : transPool.getMapDescriptor().getStops().getStop()) {
            node = sm.getOrCreate(station.getX(), station.getY());
            node.setName(station.getName());
            model.addCell(node);
        }

        return sm;
    }
//    private StationManager createStations1(Model model) {
//        StationManager sm = new StationManager(StationNode::new);
//
//        StationNode station = sm.getOrCreate(2, 2);
//        station.setName("This is a test for long string");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            trips.add("Mosh");
//            trips.add("Menash");
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        station = sm.getOrCreate(5, 5);
//        station.setName("B");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        station = sm.getOrCreate(7, 9);
//        station.setName("C");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            trips.add("Mosh");
//            trips.add("Menash");
//            trips.add("Tikva");
//            trips.add("Mazal");
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        station = sm.getOrCreate(4, 6);
//        station.setName("D");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            trips.add("Mazal");
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        return sm;
//    }

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

    public Graph createGraph(Time time, TransPool transPool) {
        Graph graph = new Graph();
        final Model model = graph.getModel();
        graph.beginUpdate();
        StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        createEdges(model, cm);
        graph.endUpdate();
        graph.getCanvas().setMaxWidth(1030);
        graph.getCanvas().setPrefWidth(1030);
        graph.getCanvas().setPrefHeight(800);
        graph.getCanvas().setMaxHeight(800);
        graph.layout(new MapGridLayout(cm, sm));
        EventHandler<MouseEvent> mouseEventEventHandler =  graph.getViewportGestures().getOnMouseDraggedEventHandler();

        //graph.getViewportGestures().setZoomSpeed(1);
        return graph;
    }
}
