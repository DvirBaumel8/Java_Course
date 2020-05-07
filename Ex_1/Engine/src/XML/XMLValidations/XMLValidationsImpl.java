package XML.XMLValidations;
import XML.XMLLoading.jaxb.schema.generated.*;

import java.io.File;
import java.util.*;

public class XMLValidationsImpl implements XMLValidations {
    private static final String VALID_XML_MESSAGE = "XML file was loaded to the system successfully\n";
    private TransPool transPool;

    public XMLValidationsImpl(TransPool transPool) {
        this.transPool = transPool;
    }

    public boolean validateXmlFile(List<String> errors, String pathToTheXMLFile) {
        List<Stop> stops = transPool.getMapDescriptor().getStops().getStop();
        List<Path> paths = transPool.getMapDescriptor().getPaths().getPath();

        List<TransPoolTrip> transPoolTrips = transPool.getPlannedTrips().getTransPoolTrip();
        MapDescriptor mapDescriptor = transPool.getMapDescriptor();

        int mapLength = mapDescriptor.getMapBoundries().getLength();
        int mapWidth = mapDescriptor.getMapBoundries().getWidth();

        boolean isValid = true;

        if(!validateFileExistsAndXmlFile(pathToTheXMLFile)) {
            errors.add("   File doesnt exists/XmlFile\n");
            isValid = false;
        }
        if (!validateMapSize(mapLength, mapWidth)) {
            errors.add("   Map boundaries aren't from 6 to 100\n");
            isValid = false;
        }
        if (!validateUniqueNameStations(stops)) {
            errors.add("   Duplicate station name\n");
            isValid = false;
        }
        if (!validateStationsBorders(stops, mapLength, mapWidth)) {
            errors.add("   Stations locations should be inside the map boundaries\n");
            isValid = false;
        }
        if (!validateStationsUniqueLocations(stops)) {
            errors.add("   Stations locations should be unique\n");
            isValid = false;
        }
        if (!validateEachWayDefinedFromDefinedStations(paths)) {
            errors.add("   Each path contains only exist and define stations\n");
            isValid = false;
        }
        if (!validateEachRoutePassesOnlyThroughDefinedStations(transPoolTrips, mapDescriptor)) {
            errors.add("   Each trip suggest contains only exist and defined stations\n");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateFileExistsAndXmlFile(String myPathToTheXMLFile) {
        File xml;
        try {
            xml = new File(myPathToTheXMLFile);
            return xml.exists() && xml.getName().endsWith(".xml");
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateMapSize(int mapLength, int mapWidth) {
        boolean isMapSizeValidate = false;
        if ((mapLength >= MAP_BOUNDARIES[0] && mapLength <= MAP_BOUNDARIES[1])
                || (mapWidth >= MAP_BOUNDARIES[0] && mapWidth <= MAP_BOUNDARIES[1])) {
            isMapSizeValidate = true;
        }
        return isMapSizeValidate;
    }

    @Override
    public boolean validateUniqueNameStations(List<Stop> stops) {
        Set<Stop> myStops = new HashSet<>();
        for (Stop stop : stops) {
            if(!myStops.add(stop)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean validateStationsBorders(List<Stop> stops, int mapLength, int mapWidth) {
        boolean isValidateStationsBorders = true;

        for (Stop stop : stops) {
            if (stop.getX() <= mapWidth && stop.getY() >= mapLength) {
                isValidateStationsBorders = false;
            }
        }
        return isValidateStationsBorders;
    }

    @Override
    public boolean validateStationsUniqueLocations(List<Stop> stops) {
        boolean isValidateUniqueLocation = true;

        for (Stop stop1 : stops) {
            for (Stop stop2 : stops) {
                if (!stop1.equals(stop2)) {
                    if ((stop1.getX() == stop2.getX()) &&
                            (stop1.getY() == stop2.getY())) {
                        isValidateUniqueLocation = false;
                    }
                }
            }
        }
        return isValidateUniqueLocation;
    }

    @Override
    public boolean validateEachWayDefinedFromDefinedStations(List<Path> paths) {
        boolean isValida = true;

        for (Path path : paths) {
            String from = path.getFrom();
            String to = path.getTo();

            if (!(checkStopStationExist(from) && checkStopStationExist(to))) {
                isValida = false;
            }
        }

        return isValida;
    }

    @Override
    public boolean validateEachRoutePassesOnlyThroughDefinedStations(List<TransPoolTrip> transPoolTrips, MapDescriptor mapDescriptor) {

        for(TransPoolTrip trip : transPoolTrips) {
            String[] route = trip.getRoute().getPath().split(",");
            if(!checkIfRouteIsLegal(route, mapDescriptor.getPaths().getPath())) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfRouteIsLegal(String[] route, List<Path> paths) {
        for(int i =0; i < route.length - 1; i++) {
            if(!checkIfPathLegal(route[i].trim(), route[i + 1].trim(), paths)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfPathLegal(String from, String to, List<Path> paths) {
        for(Path path : paths) {
            if( (path.getFrom().equals(from) && path.getTo().equals(to) ) || (path.getFrom().equals(to) && path.getTo().equals(from) && !path.isOneWay())) {
                return true;
            }
        }

        return false;
    }

    public boolean checkStopStationExist(String stationName) {


        for(Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            if(stop.getName().equals(stationName)) {
                return true;
            }
        }

        return false;
    }

    public static String getValidXmlMessage() {
        return VALID_XML_MESSAGE;
    }
}