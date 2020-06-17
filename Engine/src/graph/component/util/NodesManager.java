package graph.component.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodesManager<N> {
    private Map<String, N> nodes;
    private BiFunction<Integer, Integer, N> factory;

    public NodesManager(BiFunction<Integer, Integer, N> factory) {
        nodes = new HashMap<>();
        this.factory = factory;
    }

    public N getOrCreate(int x, int y) {
        String coordinateKey = assembleKey(x, y);
        return nodes.computeIfAbsent(coordinateKey, k -> factory.apply(x, y) );
    }

    public List<N> getAllCoordinates() {
        return new ArrayList<>(nodes.values());
    }

    private String assembleKey(int x, int y) {
        return x + "-" + y;
    }
}
