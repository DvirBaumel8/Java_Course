package graph.component.station;

import graph.component.util.NodesManager;

import java.util.function.BiFunction;

public class StationManager  extends NodesManager<StationNode> {
    public StationManager(BiFunction<Integer, Integer, StationNode> factory) {
        super(factory);
    }
}
