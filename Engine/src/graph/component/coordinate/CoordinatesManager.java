package graph.component.coordinate;

import graph.component.util.NodesManager;

import java.util.function.BiFunction;

public class CoordinatesManager extends NodesManager<CoordinateNode> {
    public CoordinatesManager(BiFunction<Integer, Integer, CoordinateNode> factory) {
        super(factory);
    }
}
