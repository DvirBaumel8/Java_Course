package Engine.XMLValidations;

public interface XMLValidator {
    public boolean validateFileExistsAndXmlFile(String fileName);

    public boolean validateMapSize();

    public boolean validateUniqueStations();

    public boolean validateStationsBorders();

    public boolean validateStationsUniqueLocations();

    public boolean validateEachDrivePassThroughStations();
}
