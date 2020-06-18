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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class GraphBuilderUtil {
    private TransPool transPool;

    public GraphBuilderUtil(TransPool transPool) {
        this.transPool = transPool;
    }

    private void createEdges(Model model, CoordinatesManager cm) {
        ArrowEdge arrowEdge;
        List<Path> pathList = transPool.getMapDescriptor().getPaths().getPath();
        List<Stop> stopStations = transPool.getMapDescriptor().getStops().getStop();

        for(Stop stop1 : stopStations) {
            for(Stop stop2 : stopStations) {
                for(Path path : pathList) {
                    String from = path.getFrom();
                    String to = path.getTo();
                    boolean isOneWay = path.isOneWay();
                    if(from.equals(stop1.getName()) && to.equals(stop2.getName())) {
                        ArrowEdge edge = new ArrowEdge(cm.getOrCreate(stop1.getX(),stop1.getY())
                                , cm.getOrCreate(stop2.getX(),stop2.getY()));
                        edge.textProperty().set(String.valueOf(path.getLength()));
                        model.addEdge(edge); // 1-3
                        if(!isOneWay) {
                            ArrowEdge edge2 = new ArrowEdge(cm.getOrCreate(stop2.getX(),stop2.getY())
                                    , cm.getOrCreate(stop1.getX(),stop1.getY()));
                            model.addEdge(edge2); // 1-3
                        }
                    }
                }
            }
        }
    }

    private CoordinatesManager createCoordinates(Model model) {
        CoordinatesManager cm = new CoordinatesManager(CoordinateNode::new);
        List<Stop> stopStations = transPool.getMapDescriptor().getStops().getStop();
        int mapLength = transPool.getMapDescriptor().getMapBoundries().getLength();
        int mapWidth = transPool.getMapDescriptor().getMapBoundries().getWidth();

        for (int i=0; i<mapLength; i++) {
            for (int j = 0; j < mapWidth; j++) {
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
