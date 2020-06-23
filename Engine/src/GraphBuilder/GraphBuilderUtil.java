package GraphBuilder;

import Manager.EngineManager;
import Time.Time;
import XML.XMLLoading.jaxb.schema.generated.Path;
import XML.XMLLoading.jaxb.schema.generated.Stop;
import XML.XMLLoading.jaxb.schema.generated.TransPool;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import graph.component.coordinate.CoordinateNode;
import graph.component.coordinate.CoordinatesManager;
import graph.component.road.ArrowEdge;
import graph.component.station.StationManager;
import graph.component.station.StationNode;
import graph.layout.MapGridLayout;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GraphBuilderUtil {
    private TransPool transPool;

    private static Graph graph = null;

    private static CoordinatesManager coordinatesManager = null;

    private static Model model= null;

    public GraphBuilderUtil(TransPool transPool) {
        this.transPool = transPool;
    }

    private void createEdges(Model model, CoordinatesManager cm) {
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

    public Graph setAndGetGraphByCurrentTripSuggest(String currentTripSuggestDetails) {
        Graph graphToSet = this.graph;
        String[] inputs = currentTripSuggestDetails.split(",");
        String currUserStation = inputs[2];
        String[] currRoute = inputs[3].split(",");
        int routeSize = currRoute.length - 1;
        List<Stop> stopStations = transPool.getMapDescriptor().getStops().getStop();
        List<Path> pathList = transPool.getMapDescriptor().getPaths().getPath();

        for(int index = 0 ; index <  routeSize ; index++) {
            String from = currRoute[0];
            String to = currRoute[1];
            Stop fromStopObject = getStopObjectFromStationName(from, stopStations);
            Stop toStopObject = getStopObjectFromStationName(to, stopStations);

            Path path = getSpecificPath(pathList, from, to);

            ArrowEdge edge = new ArrowEdge(coordinatesManager.getOrCreate(fromStopObject.getX(),fromStopObject.getY())
                    , coordinatesManager.getOrCreate(toStopObject.getX(),toStopObject.getY()));
            edge.textProperty().set(String.valueOf(path.getLength()));
            model.addEdge(edge);

            /*
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
 */
        }




        return null;
    }

    private Stop getStopObjectFromStationName(String stationName, List<Stop> stopStations) {
        Stop res = new Stop();
        res.setName(stationName);

        stopStations.forEach((currStopOfTheRoute) -> {
            if(stationName.equals(currStopOfTheRoute)) {
                res.setX(currStopOfTheRoute.getX());
                res.setY(currStopOfTheRoute.getY());
            }
        });

        return res;
    }

    private Path getSpecificPath(List<Path> pathList, String fromStr, String toStr) {
        Path resPath = null;

        for(Path path : pathList) {
            if(path.equals(fromStr) && path.equals(toStr)) {
                resPath = path;
            }
        }

        return resPath;
    }

    private int findCoordinateYToStation(String sourceStation) {
        return EngineManager.getEngineManagerInstance().getXCoorOfStation(sourceStation);
    }

    private int findCoordinateXToStation(String sourceStation) {
        return EngineManager.getEngineManagerInstance().getYCoorOfStation(sourceStation);
    }

    private CoordinatesManager createCoordinates(Model model) {
        coordinatesManager = new CoordinatesManager(CoordinateNode::new);
        List<Stop> stopStations = transPool.getMapDescriptor().getStops().getStop();
        int mapLength = transPool.getMapDescriptor().getMapBoundries().getLength();
        int mapWidth = transPool.getMapDescriptor().getMapBoundries().getWidth();

        for (int i=0; i<mapLength; i++) {
            for (int j = 0; j < mapWidth; j++) {
                model.addCell(coordinatesManager.getOrCreate(i+1, j+1));
            }
        }

        return coordinatesManager;
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
        graph = new Graph();
        model = graph.getModel();
        graph.beginUpdate();
        coordinatesManager = createCoordinates(model);
        StationManager sm = createStations(model);
        createEdges(model, coordinatesManager);
        graph.endUpdate();
        graph.getCanvas().setMaxWidth(1030);
        graph.getCanvas().setPrefWidth(1030);
        graph.getCanvas().setPrefHeight(800);
        graph.getCanvas().setMaxHeight(800);
        graph.layout(new MapGridLayout(coordinatesManager, sm));
        EventHandler<MouseEvent> mouseEventEventHandler =  graph.getViewportGestures().getOnMouseDraggedEventHandler();

        //graph.getViewportGestures().setZoomSpeed(1);
        return graph;
    }
}
