package Engine.XMLValidations;

import Engine.XMLLoading.jaxb.schema.generated.MapDescriptor;
import Engine.XMLLoading.jaxb.schema.generated.Path;
import Engine.XMLLoading.jaxb.schema.generated.Stop;
import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.util.List;

public interface XMLValidator {

    static final Integer[] MAP_BOUNDARIES = {6,100};

     boolean validateFileExistsAndXmlFile(String myPathToTheXMLFile);

     boolean validateMapSize(int mapLength, int mapWidth);

     boolean validateUniqueNameStations(List<Stop> stops, int mapLength, int mapWidth);

     boolean validateStationsBorders(List<Stop> stops, int mapLength, int mapWidth);

     boolean validateStationsUniqueLocations(List<Stop> stops);

     boolean validateEachWayDefinedFromDefinedStations(List<Path> paths);

     boolean validateEachRoutePassesOnlyThroughDefinedStations(List<TransPoolTrip> transPoolTrips,
                                                               MapDescriptor mapDescriptor);
}
