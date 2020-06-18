package MatchingUtil;

import Manager.EngineManager;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.LinkedList;

public class CalculatorUtil {

    public static int calcCost(int ppk, LinkedList<Station> stations) {
        int sum = 0;
        for(int i = 0; i < stations.size() - 1; i++) {
            int km = getLengthBetweenStations(stations.get(i), stations.get(i+1));
            sum += km * ppk;
        }
        return sum;
    }

    private static int getLengthBetweenStations(Station pathFrom, Station pathTo) {
        return EngineManager.getEngineManagerInstance().getLengthBetweenStations(pathFrom.getName(), pathTo.getName());
    }

    public static int calcRequiredFuel(LinkedList<Station> stations) {
        return EngineManager.getEngineManagerInstance().getRequiredFuelToPath(stations.getFirst().getName(), stations.getLast().getName());
    }
}
