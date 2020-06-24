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
import java.util.LinkedList;
import java.util.List;

public class GraphBuilderUtil {
    private TransPool transPool;

    private static Graph graph = null;

    private static CoordinatesManager coordinatesManager = null;

    private static StationManager stationManager = null;

    private static Model model = null;

    public GraphBuilderUtil(TransPool transPool) {
        this.transPool = transPool;
    }

    private void createEdges() {
        List<Path> pathList = transPool.getMapDescriptor().getPaths().getPath();
        List<Stop> stopStations = transPool.getMapDescriptor().getStops().getStop();

        for(Stop stop1 : stopStations) {
            for(Stop stop2 : stopStations) {
                for(Path path : pathList) {
                    String from = path.getFrom();
                    String to = path.getTo();
                    boolean isOneWay = path.isOneWay();
                    if(from.equals(stop1.getName()) && to.equals(stop2.getName())) {
                        ArrowEdge edge = new ArrowEdge(coordinatesManager.getOrCreate(stop1.getX(),stop1.getY())
                                , coordinatesManager.getOrCreate(stop2.getX(),stop2.getY()));
                        edge.textProperty().set(String.valueOf(path.getLength()));

                        model.addEdge(edge); // 1-3
                        if(!isOneWay) {
                            ArrowEdge edge2 = new ArrowEdge(coordinatesManager.getOrCreate(stop2.getX(),stop2.getY())
                                    , coordinatesManager.getOrCreate(stop1.getX(),stop1.getY()));
                            model.addEdge(edge2); // 1-3
                        }
                    }
                }
            }
        }
    }

    public Graph setAndGetGraphByCurrentTripSuggest(String currentTripSuggestDetails) {
        // mark path station in unique color plus mark the path in uniqe color

        String[] inputs = currentTripSuggestDetails.split(",");
        String suggestId = inputs[1];
        String roadTrip = inputs[2];
        String currUserStation = inputs[3];
        suggestId = suggestId.substring(4);
        roadTrip = roadTrip.substring(8);
        currUserStation = currUserStation.substring(12);
        String[] currRoute = roadTrip.split("-");
        List<Stop> suggestRoadTripStations = getSuggestRoadTripStationsFromPathArr(currRoute);
        Stop currStation = getStopObjectFromStationName(currUserStation, suggestRoadTripStations);

        markSuggestTripStationsAndCurrStation(suggestRoadTripStations, currStation, suggestId);
        graph.endUpdate();
        graph.getCanvas().setMaxWidth(1030);
        graph.getCanvas().setPrefWidth(1030);
        graph.getCanvas().setPrefHeight(800);
        graph.getCanvas().setMaxHeight(800);
        graph.layout(new MapGridLayout(coordinatesManager, stationManager));

        return null;
    }

    private List<Stop> getSuggestRoadTripStationsFromPathArr(String[] currRoute) {
        List<Stop> suggestRoadTripStations = new LinkedList<>();
        int index = 0;
        List<Stop> transPoolStopList = transPool.getMapDescriptor().getStops().getStop();

        for(int i = 0 ; i < transPoolStopList.size() ; i ++) {
            if(transPoolStopList.get(i).getName().equals(currRoute[index])) {
                suggestRoadTripStations.add(transPoolStopList.get(i));
                index ++;
            }
        }

        return suggestRoadTripStations;
    }



    private Stop getStopObjectFromStationName(String stationName, List<Stop> stopStations) {
        Stop res = new Stop();
        res.setName(stationName);

        for(Stop stop : stopStations) {
            String stopStr = stop.getName();
            if(stopStr.equals(res)) {
                res.setX(stop.getX());
                res.setY(stop.getY());
            }
        }

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

    private void createCoordinates() {
        coordinatesManager = new CoordinatesManager(CoordinateNode::new);
        //List<Stop> stopStations = transPool.getMapDescriptor().getStops().getStop();
        int mapLength = transPool.getMapDescriptor().getMapBoundries().getLength();
        int mapWidth = transPool.getMapDescriptor().getMapBoundries().getWidth();

        for (int i=0; i<mapLength; i++) {
            for (int j = 0; j < mapWidth; j++) {
                model.addCell(coordinatesManager.getOrCreate(i+1, j+1));
            }
        }

    }

    private void createStations() {
        stationManager = new StationManager(StationNode::new);
        StationNode node;

        for(Stop station : transPool.getMapDescriptor().getStops().getStop()) {
            node = stationManager.getOrCreate(station.getX(), station.getY());
            node.setName(station.getName());
            model.addCell(node);
        }
    }

    private StationManager markSuggestTripStationsAndCurrStation(List<Stop> suggestRoadTripStations, Stop currStation,
    String suggestId) {
        StationNode node;
        int index = 1;

        for(Stop station : suggestRoadTripStations) {
            if(currStation.equals(station.getName())) {
                node = stationManager.getOrCreate(station.getX(), station.getY());
                node.setName(station.getName() + suggestId +",This is curr station! number " + index + "at the road trip");
                model.addCell(node);
            }
            else {
                node = stationManager.getOrCreate(station.getX(), station.getY());
                node.setName(station.getName() + suggestId + ",station number:" + index + "at the road trip" );
                model.addCell(node);
            }
            index++;
        }

        return stationManager;
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
        createCoordinates();
        createStations();
        createEdges();
        graph.endUpdate();
        graph.getCanvas().setMaxWidth(1030);
        graph.getCanvas().setPrefWidth(1030);
        graph.getCanvas().setPrefHeight(800);
        graph.getCanvas().setMaxHeight(800);
        graph.layout(new MapGridLayout(coordinatesManager, stationManager));

        //graph.getViewportGestures().setZoomSpeed(1);
        return graph;
    }
}
