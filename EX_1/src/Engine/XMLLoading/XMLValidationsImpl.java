package Engine.XMLLoading;

import Engine.XMLLoading.jaxb.schema.generated.Stop;
import Engine.XMLLoading.jaxb.schema.generated.TransPool;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class XMLValidationsImpl implements XMLValidator {
    private static final String VALID_XML_MESSAGE = "XML file was loaded to the system successfully";
    private String errorMessage;
    TransPool transPool;

    public XMLValidationsImpl(TransPool transPool) {
        this.transPool = transPool;
    }

    public boolean validateXmlFile (String myPathToTheXMLFile, List<String> errors) {
        boolean isValid = true;
        List<Stop> stops = transPool.getMapDescriptor().getStops().getStop();
        int mapLength = transPool.getMapDescriptor().getMapBoundries().getLength();
        int mapWidth = transPool.getMapDescriptor().getMapBoundries().getWidth();

        if(!validateFileExistsAndXmlFile(myPathToTheXMLFile)) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: File Doesnt exist/Not XML type");
            isValid = false;
        }
        if(!validateMapSize(mapLength, mapWidth)) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Map size ranges isnt from 6 to 100, inclusive, for both length and width");
            isValid = false;
        }
        if(!validateUniqueNameStations(stops, mapLength, mapWidth)) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Each station doesnt have its own unique name");
            isValid = false;
        }
        if(validateStationsBorders(stops, mapLength, mapWidth)) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Each station is NOT defined within the boundaries of the map");
            isValid = false;
        }
        if(validateStationsUniqueLocations(stops)) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Each station is NOT a unique location" +
                    " (no 2 stations located in the same coordinates)");
            isValid = false;
        }
        if(validateEachWayDefinedFromDefinedStations(stops)) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE:Each route is NOT defined as a route" +
                    " that passes only through defined stations and routes");
            isValid = true;
        }
        if(validateEachRoutePassesOnlyThroughDefinedStations()) {
            checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE:Each route is NOT defined as a route" +
                    " that passes only through defined stations and routes");
            isValid = true;
        }
        return isValid;
    }

    @Override
    public boolean validateFileExistsAndXmlFile(String myPathToTheXMLFile) {
        File xml =  null;
        xml = new File(myPathToTheXMLFile);
        boolean exists = xml.exists();
        return xml.getName().contains(".xml");
    }

    public void checkIfErrorListNullableAndInitialize(List<String> errors) {
        if(errors == null) {
            errors = new LinkedList<>();
        }
    }

    @Override
    public boolean validateMapSize(int mapLength, int mapWidth) {
        boolean isMapSizeValidate = false;
            if((mapLength >= XMLValidator.MAP_BOUNDRIES[0] && mapLength <= XMLValidator.MAP_BOUNDRIES[1])
                    || (mapWidth >= XMLValidator.MAP_BOUNDRIES[0] && mapWidth <= XMLValidator.MAP_BOUNDRIES[1])) {
                isMapSizeValidate = true;
            }
        return isMapSizeValidate;
    }

    @Override
    public boolean validateUniqueNameStations(List<Stop> stops,int mapLength, int mapWidth) {
        Set<String> checkUniqueStationsDS = new HashSet<>();
        boolean isValidateUniqueStations = true;

        for(Stop stop : stops){
            if(checkUniqueStationsDS.contains(stop.getName())) {
                isValidateUniqueStations = false;
            }
            else {
                checkUniqueStationsDS.add(stop.getName());
            }
        }

        return true;
    }

    @Override
    public boolean validateStationsBorders(List<Stop> stops,int mapLength, int mapWidth) {
        boolean isValidateStationsBorders = true;

        for(Stop stop : stops){
           if(stop.getX() <=  mapWidth && stop.getY() >= mapLength) {
               isValidateStationsBorders = false;
           }
        }
        return false;
    }

    @Override
    public boolean validateStationsUniqueLocations(List<Stop> stops) {
        boolean isValidateUniqueLocation = true;

        for(Stop stop1 : stops){
            for(Stop stop2 : stops){
                if(stop1 != stop2) {
                    if((stop1.getX() == stop2.getX()) &&
                            (stop1.getY() == stop2.getY())) {
                        isValidateUniqueLocation = false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean validateEachWayDefinedFromDefinedStations(List<Stop> stops) {
        boolean isValidateUniqueLocation = true;


        return isValidateUniqueLocation;
    }

    @Override
    public boolean validateEachRoutePassesOnlyThroughDefinedStations() {
        boolean isRoutePassesOnlyThroughDefinedStations = true;


        return isRoutePassesOnlyThroughDefinedStations;
    }

    public String getValidMessage() {
        return VALID_XML_MESSAGE;
    }

    public String getErrorMessage () {
        return errorMessage;
    }

}
