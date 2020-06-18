package RootWrapper;

import com.fxgraph.graph.Graph;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class RootWrapper {
    static BorderPane root = null;

    //live map component
    static ScrollPane borderPaneLiveMapCenter = null;
    private static Graph graph = null;


    public RootWrapper() {
    }

    public static BorderPane getRoot() {
        return root;
    }

    public static void setRoot(BorderPane root) {
        RootWrapper.root = root;
    }

    public static void setRootCenter(Graph graph) {
        graph = graph;
        borderPaneLiveMapCenter = new ScrollPane();
        borderPaneLiveMapCenter.setContent(graph.getCanvas());
        RootWrapper.root.setCenter(borderPaneLiveMapCenter);
    }

    public static ScrollPane getBorderPaneLiveMapCenter() {
        return borderPaneLiveMapCenter;
    }

    public static void setBorderPaneLiveMapCenter(ScrollPane borderPaneLiveMapCenter) {
        RootWrapper.borderPaneLiveMapCenter = borderPaneLiveMapCenter;
    }

    public static Graph getGraph() {
        return graph;
    }

    public static void setGraph(Graph graph) {
        RootWrapper.graph = graph;
    }
}
