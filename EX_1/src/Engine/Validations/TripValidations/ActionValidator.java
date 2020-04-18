package Engine.Validations.TripValidations;

import Engine.Manager.EngineManager;
import Engine.XMLLoading.jaxb.schema.generated.Stop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionValidator {

    private StringBuilder generalErrorMessage;

    public ActionValidator() {
        this.generalErrorMessage = new StringBuilder();
    }

    public boolean validateOwnerName(String input) {
        try {
            Integer.parseInt(input);
            generalErrorMessage.append("*Owner name can't contains only numbers\n");
        }
        catch(Exception e) {
            return true;
        }
        if(input.isEmpty()) {
            generalErrorMessage.append("*Request owner name is empty\n");
            return false;
        }
        return true;
    }


    public boolean validateTime (String input) {
        Pattern p = Pattern.compile("[0-2][0-9]:[0-5][0-9]");
        Matcher matcher = p.matcher(input);
        if(matcher.matches()) {
            return true;
        }
        else {
            generalErrorMessage.append("*Time template isn't valid, template should be __:__ (12:34)\n");
            return false;
        }
    }

    public boolean checkIFStationsIsExist(String stationName) {
        for(Stop stop : EngineManager.getEngineManagerInstance().getTransPool().getMapDescriptor().getStops().getStop()) {
            if(stop.getName().equals(stationName)) {
                return true;
            }
        }
        return false;
    }

    public boolean validateDestination (String input) {
        if(checkIFStationsIsExist(input)) {
            return true;
        }
        else {
            generalErrorMessage.append("*Destination isn't exist in the system\n");
            return false;
        }
    }

    public boolean checkIfStringIsInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
}
