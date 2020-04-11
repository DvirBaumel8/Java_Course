package Engine.XMLLoading;

public interface XMLValidator {
    public boolean validateFileExists(String fileName);

    public boolean validateMapSize();

    public boolean validateUniqueStations();

    public boolean validateStationsBorders();

    public boolean validateStationsUniqueLocations();

    public boolean validateEachDrivePassThroughStations();
}
