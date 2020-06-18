package GraphBuilder;

import Manager.EngineManager;
import Time.Time;
import XML.XMLLoading.jaxb.schema.generated.Path;
import XML.XMLLoading.jaxb.schema.generated.Stop;
import XML.XMLLoading.jaxb.schema.generated.TransPool;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.sun.xml.internal.ws.api.pipe.Engine;
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
        String sourceStation;
        String destStation;
        int xSource;
        int ySource;
        int xDest;
        int yDest;

        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            sourceStation = path.getFrom();
            destStation = path.getTo();
            xSource = findCoordinateXToStation(sourceStation);
            ySource = findCoordinateYToStation(sourceStation);
            xDest = findCoordinateXToStation(destStation);
            yDest = findCoordinateYToStation(destStation);
            if (path.isOneWay()) {
                arrowEdge = new ArrowEdge(cm.getOrCreate(xSource, ySource), cm.getOrCreate(xDest, yDest));
                model.addEdge(arrowEdge);
            }
            else {
                arrowEdge = new ArrowEdge(cm.getOrCreate(xDest, yDest), cm.getOrCreate(xSource, ySource));
                arrowEdge = new ArrowEdge(cm.getOrCreate(xSource, ySource), cm.getOrCreate(xDest, yDest));
                model.addEdge(arrowEdge);
            }
            arrowEdge.textProperty().set(path.getFrom());
        }
    }

    private int findCoordinateYToStation(String sourceStation) {
        return EngineManager.getEngineManagerInstance().getXCoorOfStation(sourceStation);
    }

    private int findCoordinateXToStation(String sourceStation) {
        return EngineManager.getEngineManagerInstance().getYCoorOfStation(sourceStation);
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
        CoordinatesManager cm = createCoordinates(model);
        StationManager sm = createStations(model);
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

    public HashSet<String> getCurrStationNames (List<Path> pathList) {
        HashSet<String> res = new HashSet<>();

        pathList.forEach(path -> {
            String to = path.getTo();
            String from = path.getFrom();
            if(!res.contains(to)) {
                res.add(to);
            }

            if(!res.contains(from)) {
                res.add(from);
            }
        });
        return res;
    }
}
