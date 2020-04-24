package Engine.XMLValidations;

import Engine.XMLLoading.jaxb.schema.generated.*;

import java.io.File;
import java.util.*;

public class XMLValidationsImpl implements XMLValidations {
    private static final String VALID_XML_MESSAGE = "XML file was loaded to the system successfully";
    private TransPool transPool;

    public XMLValidationsImpl(TransPool transPool) {
    }

    public boolean validateXmlFile(TransPool transPoolTOCheck, List<String> errors, String pathToTheXMLFile) {
        transPool = transPoolTOCheck;
        List<Stop> stops = transPool.getMapDescriptor().getStops().getStop();
        List<Path> paths = transPool.getMapDescriptor().getPaths().getPath();

        List<TransPoolTrip> transPoolTrips = transPool.getPlannedTrips().getTransPoolTrip();
        MapDescriptor mapDescriptor = transPool.getMapDescriptor();

        int mapLength = mapDescriptor.getMapBoundries().getLength();
        int mapWidth = mapDescriptor.getMapBoundries().getWidth();

        boolean isValid = true;

        if(!validateFileExistsAndXmlFile(pathToTheXMLFile)) {
            errors.add("File doesnt exists/XmlFile\n");
            isValid = false;
        }
        if (!validateMapSize(mapLength, mapWidth)) {
            errors.add("Map size ranges isn't from 6 to 100, inclusive, for both length and width\n");
            isValid = false;
        }
        if (!validateUniqueNameStations(stops)) {
            errors.add("Each station doesnt have its own unique name\n");
            isValid = false;
        }
        if (!validateStationsBorders(stops, mapLength, mapWidth)) {
            errors.add("Each station is NOT defined within the boundaries of the map\n");
            isValid = false;
        }
        if (!validateStationsUniqueLocations(stops)) {
            errors.add("Each station is NOT a unique location (no 2 stations located in the same coordinates)\n");
            isValid = false;
        }
        if (!validateEachWayDefinedFromDefinedStations(paths)) {
            errors.add("Each route is NOT defined as a route that passes only through defined stations and routes\n");
            isValid = false;
        }
        if (!validateEachRoutePassesOnlyThroughDefinedStations(transPoolTrips, mapDescriptor)) {
            errors.add("Each route is NOT defined as a route that passes only through defined stations and routes\n");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateFileExistsAndXmlFile(String myPathToTheXMLFile) {
        File xml;
        try {
            xml = new File(myPathToTheXMLFile);
            return xml.exists() && xml.getName().contains(".xml");
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateMapSize(int mapLength, int mapWidth) {
        boolean isMapSizeValidate = false;
        if ((mapLength >= XMLValidations.MAP_BOUNDARIES[0] && mapLength <= XMLValidations.MAP_BOUNDARIES[1])
                || (mapWidth >= XMLValidations.MAP_BOUNDARIES[0] && mapWidth <= XMLValidations.MAP_BOUNDARIES[1])) {
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
    public boolean validateEachRoutePassesOnlyThroughDefinedStations(List<TransPoolTrip> transPoolTrips,
                                                                     MapDescriptor mapDescriptor) {
        boolean isRoutePassesOnlyThroughDefinedStations = true;
        try {
            List<Path> pathMapDescriptorList = mapDescriptor.getPaths().getPath();
            Map<String, List<String>> pathValidOptions = getPathValidOptions(pathMapDescriptorList);

            for (TransPoolTrip transPoolTrip : transPoolTrips) {
                String pathUserString = transPoolTrip.getRoute().getPath();
                String[] pathUserStopsArr = pathUserString.split(",");

                int loopSize = pathUserStopsArr.length - 1;
                for (int i = 0; i < loopSize; i++) {
                    String from = pathUserStopsArr[i];
                    String to = pathUserStopsArr[i + 1];
                    isRoutePassesOnlyThroughDefinedStations = checkIfRouteAvailable(pathValidOptions, from, to);
                    if (!isRoutePassesOnlyThroughDefinedStations) {
                        return isRoutePassesOnlyThroughDefinedStations;
                    }
                }
            }
        }
        catch (NullPointerException e) {

        }

        return isRoutePassesOnlyThroughDefinedStations;
    }

    public boolean checkStopStationExist(String stationName) {


        for(Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            if(stop.getName().equals(stationName)) {
                return true;
            }
        }

        return false;
    }

    public Map<String, List<String>> getPathValidOptions(List<Path> pathMapDescriptorList) {
        Map<String, List<String>> pathValidOptions = null;
        //key - from , value - list of all the Optional destination
        pathValidOptions = nullablePathMapDescriptorListCheck(pathMapDescriptorList);

        for (Path path : pathMapDescriptorList) {
            if (pathValidOptions.containsKey(path.getFrom())) {
                containKeyAction(pathValidOptions, path.getFrom(), path.getTo());
            } else {
                notContainKeyAction(pathValidOptions, path.getFrom(), path.getTo());
                if (!path.isOneWay()) {
                    twoWayAction(pathValidOptions, path.getFrom(), path.getTo());
                }
            }
        }

        return pathValidOptions;
    }

    public void containKeyAction(Map<String, List<String>> pathValidOptions, String from, String to) {
        List<String> destStops = pathValidOptions.get(from);
        boolean ifDestExists = false;
        for (String destName : destStops) {
            if (destName.equals(to)) {
                ifDestExists = true;
            }
        }
        if (!ifDestExists) {
            destStops.add(to);
            pathValidOptions.put(from, destStops);
        }
    }

    public void notContainKeyAction(Map<String, List<String>> pathValidOptions, String from, String to) {
        List<String> destStops = new LinkedList<>();
        destStops.add(to);
        pathValidOptions.put(from, destStops);
    }

    public void twoWayAction(Map<String, List<String>> pathValidOptions, String from, String to) {
        if (pathValidOptions.containsKey(to)) {
            containKeyAction(pathValidOptions, to, from);
        } else {
            notContainKeyAction(pathValidOptions, to, from);
        }
    }

    public  Map<String, List<String>> nullablePathMapDescriptorListCheck(List<Path> pathMapDescriptorList) {
        Map<String, List<String>> pathValidOptions = null;
        if (pathMapDescriptorList != null) {
            pathValidOptions = new HashMap<>();
        }
        return pathValidOptions;
    }

    public boolean checkIfRouteAvailable(Map<String, List<String>> pathValidOptions, String from, String to) {
        List<String> toStr = null;

        if (!pathValidOptions.containsKey(from)) {
            return false;
        } else {
            toStr = pathValidOptions.get(from);
            if (toStr != null) {
                return checkIfRouteAvailableHelper(toStr, to);
            } else {
                return false;
            }
        }
    }

    public boolean checkIfRouteAvailableHelper(List<String> toStr, String to) {
        boolean validationCheck = false;
        for (String currTo : toStr) {
            if (currTo.equals(to)) {
                validationCheck = true;
            }
        }
        if (!validationCheck) {
            return false;
        } else {
            return true;
        }
    }

    public static String getValidXmlMessage() {
        return VALID_XML_MESSAGE;
    }

    public String getValidMessage() {
        return VALID_XML_MESSAGE;
    }
}