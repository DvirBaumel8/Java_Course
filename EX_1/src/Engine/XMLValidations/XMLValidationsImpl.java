package Engine.XMLValidations;

public class XMLValidationsImpl implements XMLValidator {
    private static final String VALID_XML_MESSAGE = "XML file was loaded to the system successfully";
    private String errorMessage;

    @Override
    public boolean validateFileExists(String fileName) {
        return false;
    }

    @Override
    public boolean validateMapSize() {
        return false;
    }

    @Override
    public boolean validateUniqueStations() {
        return false;
    }

    @Override
    public boolean validateStationsBorders() {
        return false;
    }

    @Override
    public boolean validateStationsUniqueLocations() {
        return false;
    }

    @Override
    public boolean validateEachDrivePassThroughStations() {
        return false;
    }

}
