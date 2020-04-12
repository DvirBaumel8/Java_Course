package Engine.XMLLoading;

import Engine.XMLLoading.jaxb.schema.generated.Stop;

import java.util.List;

public interface XMLValidator {

    static final Integer[] MAP_BOUNDRIES = {6,100};

    public boolean validateFileExistsAndXmlFile(String myPathToTheXMLFile);

    public boolean validateMapSize(int mapLength, int mapWidth);

    public boolean validateUniqueNameStations(List<Stop> stops, int mapLength, int mapWidth);

    public boolean validateStationsBorders(List<Stop> stops, int mapLength, int mapWidth);

    public boolean validateStationsUniqueLocations(List<Stop> stops);

    public boolean validateEachWayDefinedFromDefinedStations(List<Stop> stops);

    public boolean validateEachRoutePassesOnlyThroughDefinedStations();
}
