package Engine.XMLValidations;

import java.io.File;

public class XMLValidationsImpl implements XMLValidator {
    private static final String VALID_XML_MESSAGE = "XML file was loaded to the system successfully";
    private String errorMessage;

    @Override
    public boolean validateFileExistsAndXmlFile(String fileName) {
        return fileName.contains(".xml");
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

    public boolean validateXmlFile (File file) {
        boolean isValid = true;

        if(!validateFileExistsAndXmlFile(file.getName())) {
            isValid = false;
        }
        if(validateMapSize()) {
            isValid = false;
        }
        if(validateUniqueStations()) {
            isValid = false;
        }
        if(validateStationsBorders()) {
            isValid = false;
        }
        if(validateStationsUniqueLocations()) {
            isValid = false;
        }
        if(validateEachDrivePassThroughStations()) {
            isValid = true;
        }
        return isValid;
    }

    public String getValidMessage() {
        return VALID_XML_MESSAGE;
    }

    public String getErrorMessage () {
        return errorMessage;
    }

}
