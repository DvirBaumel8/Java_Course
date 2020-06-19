package controllers;

import RootWrapper.RootWrapper;
import com.fxgraph.graph.Graph;

public class LiveMapController {
    private AppController mainController;

    private static RootWrapper rootWrapper = null;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public static RootWrapper getRootWrapper() {
        return rootWrapper;
    }

    public static void setRootWrapper(RootWrapper rootWrapper) {
        LiveMapController.rootWrapper = rootWrapper;
    }

    public void setLiveMapToRootCenter(Graph graph) {
        rootWrapper = new RootWrapper();
        rootWrapper.setRootCenter(graph);
    }
}
