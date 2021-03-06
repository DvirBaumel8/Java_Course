package RootWrapper;

import com.fxgraph.graph.Graph;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

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
        borderPaneLiveMapCenter.setBackground((new Background(new BackgroundFill(Color.LIGHTYELLOW,
                CornerRadii.EMPTY, Insets.EMPTY))));
        RootWrapper.root.setCenter(borderPaneLiveMapCenter);
    }

    public static Graph getGraph() {
        return graph;
    }

    public static void setGraph(Graph graph) {
        RootWrapper.graph = graph;
    }
}
