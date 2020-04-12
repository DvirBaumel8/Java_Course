package Engine.Manager;

import Engine.TripRequests.TripRequest;
import Engine.TripRequests.TripRequestsUtil;
import Engine.XMLLoading.XMLValidationsImpl;
import Engine.XMLLoading.jaxb.schema.SchemaBasedJAXBMain;
import Engine.XMLLoading.jaxb.schema.generated.PlannedTrips;
import Engine.XMLLoading.jaxb.schema.generated.Stop;
import Engine.XMLLoading.jaxb.schema.generated.TransPool;
import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.io.File;
import java.util.List;

public class EngineManager {
    private static EngineManager engineManager;
    private TransPool transPool;
    private TripRequestsUtil tripRequestUtil = new TripRequestsUtil();

    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManager == null) {
            engineManager = new EngineManager();
        }
        return engineManager;
    }

    public String LoadXML(String myPathToTheXMLFile, List<String> errors) {
        SchemaBasedJAXBMain jax = new SchemaBasedJAXBMain();
        transPool = jax.init();

        XMLValidationsImpl xmlValidator = new XMLValidationsImpl(transPool);
        if (xmlValidator.validateXmlFile(myPathToTheXMLFile, errors)) {
            return xmlValidator.getValidMessage();
        } else {
           return xmlValidator.getErrorMessage();
        }
    }

    public String getAllStationsName () {
        StringBuilder str = new StringBuilder();
        str.append("All stations names: \n");
        int index = 1;
        for(Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            str.append(String.format("%d - %s\n",index, stop.getName()));
            index++;
        }
        return str.toString();
    }

    public String getAllSuggestedTrips () {
        StringBuilder str = new StringBuilder();
        str.append("All suggested trips: \n");
        int index = 1;
        for(TransPoolTrip trip : transPool.getPlannedTrips().getTransPoolTrip()) {
            str.append(String.format("%d - \n", index));
            index++;
            str.append(String.format("Trip ID - %s\n", null)); // have to fix
            str.append(String.format("Trip Owner - %s\n", trip.getOwner()));
            str.append(String.format("Trip Route - %s\n", trip.getRoute()));
            str.append(String.format("Trip Price - %s\n", trip.getPPK())); //have to fix
            str.append(String.format("Trip starting hour - %s, Trip arrival hour - %s\n", trip.getScheduling().getHourStart(), trip.getScheduling().getDayStart())); // have to fix
            str.append(String.format("Trip available sits - %s\n", trip.getCapacity())); // have to fix
            str.append(String.format("Exists passengers trip - %s\n", null));// have to fix
            str.append(String.format("Trip ID - %s\n", null));
            str.append(String.format("Trip ID - %s\n", null));
        }
        return str.toString();
    }

    public String getAllTripRequests () {
        StringBuilder str = new StringBuilder();
        str.append("All requested trips\n");
        for(TransPoolTrip trip : transPool.getPlannedTrips().getTransPoolTrip()) {
            str.append(String.format("Trip ID - %s", null)); // have to fix
            str.append(String.format("Trip requester - %s", trip.getOwner())); // have to fix
            str.append(String.format("Trip starting station - %s Trip Arrival station - ", trip.getRoute().getPath(), null)); // have to fix
            str.append(String.format("Trip starting hour - %s", trip.getScheduling().getHourStart())); // have to fix
        }
        return str.toString();
    }

    public void createNewTripRequest(String input) {
        String[] inputs = input.split(",");
        TripRequest newRequest = new TripRequest(inputs[0], inputs[1], inputs[2], Integer.parseInt(inputs[3]));
        tripRequestUtil.addRequestTrip(newRequest);
    }

    public boolean validateTripRequestInput(String input) {
        return true;
    }
}

