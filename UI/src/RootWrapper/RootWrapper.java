package RootWrapper;

import com.fxgraph.graph.Graph;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class RootWrapper {
    static BorderPane root = null;

    public RootWrapper() {
    }

    public static BorderPane getRoot() {
        return root;
    }

    public static void setRoot(BorderPane root) {
        RootWrapper.root = root;
    }

    public static void setRootCenter(Graph graph) {
        ScrollPane borderPaneLiveMapCenter = new ScrollPane();
        borderPaneLiveMapCenter.setContent(graph.getCanvas());
        RootWrapper.root.setCenter(borderPaneLiveMapCenter);
    }
}
