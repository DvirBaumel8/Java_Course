package Engine.XMLValidations;

import Engine.XMLLoading.jaxb.schema.generated.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class XMLValidationsImpl implements XMLValidator {
    private static final String VALID_XML_MESSAGE = "XML file was loaded to the system successfully";
    private String errorMessage;
    TransPool transPool;
    Map<String, Stop> existingAndDefinedStops = null;

    public XMLValidationsImpl(TransPool transPool) {
        this.transPool = transPool;
    }

    public List<String> validateXmlFile(String myPathToTheXMLFile) {
        List<String> errors = null;
        List<Stop> stops = transPool.getMapDescriptor().getStops().getStop();
        List<Path> paths = transPool.getMapDescriptor().getPaths().getPath();
        List<TransPoolTrip> transPoolTrips = transPool.getPlannedTrips().getTransPoolTrip();
        MapDescriptor mapDescriptor = transPool.getMapDescriptor();
        int mapLength = mapDescriptor.getMapBoundries().getLength();
        int mapWidth = mapDescriptor.getMapBoundries().getWidth();

        if (!validateFileExistsAndXmlFile(myPathToTheXMLFile)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: File Doesnt exist/Not XML type");
        }
        if (!validateMapSize(mapLength, mapWidth)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Map size ranges isnt from 6 to 100, inclusive, for both length and width");
        }
        if (!validateUniqueNameStations(stops, mapLength, mapWidth)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Each station doesnt have its own unique name");
        }
        if (!validateStationsBorders(stops, mapLength, mapWidth)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Each station is NOT defined within the boundaries of the map");
        }
        if (!validateStationsUniqueLocations(stops)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE: Each station is NOT a unique location" +
                    " (no 2 stations located in the same coordinates)");
        }
        if (!validateEachWayDefinedFromDefinedStations(paths)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE:Each route is NOT defined as a route" +
                    " that passes only through defined stations and routes");
        }
        if (!validateEachRoutePassesOnlyThroughDefinedStations(transPoolTrips, mapDescriptor)) {
            errors = checkIfErrorListNullableAndInitialize(errors);
            errors.add("ERROR TYPE:Each route is NOT defined as a route" +
                    " that passes only through defined stations and routes");
        }
        return errors;
    }

    @Override
    public boolean validateFileExistsAndXmlFile(String myPathToTheXMLFile) {
        File xml = null;
            xml = new File(myPathToTheXMLFile);

        boolean exists = xml.exists();
        return xml.getName().contains(".xml");
    }

    public List<String>  checkIfErrorListNullableAndInitialize(List<String> errors) {
        List<String> res = null;
        if (errors == null) {
            res = new LinkedList<>();
            return res;
        }
        else {
            return errors;
        }
    }

    @Override
    public boolean validateMapSize(int mapLength, int mapWidth) {
        boolean isMapSizeValidate = false;
        if ((mapLength >= XMLValidator.MAP_BOUNDARIES[0] && mapLength <= XMLValidator.MAP_BOUNDARIES[1])
                || (mapWidth >= XMLValidator.MAP_BOUNDARIES[0] && mapWidth <= XMLValidator.MAP_BOUNDARIES[1])) {
            isMapSizeValidate = true;
        }
        return isMapSizeValidate;
    }

    @Override
    public boolean validateUniqueNameStations(List<Stop> stops, int mapLength, int mapWidth) {
        existingAndDefinedStops = new HashMap<>();
        boolean isValidateUniqueStations = true;

        for (Stop stop : stops) {
            if (existingAndDefinedStops.containsKey(stop.getName())) {
                isValidateUniqueStations = false;
            } else {
                existingAndDefinedStops.put(stop.getName(), stop);
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
        boolean isValidateUniqueLocation = true;
        for (Path path : paths) {
            String from = path.getFrom();
            String to = path.getTo();
            if (!checkStopsValidations(from, to)) {
                isValidateUniqueLocation = false;
            }
        }
        return isValidateUniqueLocation;
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

    public boolean checkStopsValidations(String from, String to) {
        boolean isValid = false;

        if (existingAndDefinedStops.containsKey(from) && existingAndDefinedStops.containsKey(to)) {
            isValid = true;
        }

        return isValid;
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
}
