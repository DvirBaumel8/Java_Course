package Engine.Manager;

import Engine.TripRequests.TripRequest;
import Engine.TripRequests.TripRequestsUtil;
import Engine.TripSuggestUtil.TripSuggestUtil;
import Engine.XMLValidations.XMLValidationsImpl;
import Engine.XMLLoading.jaxb.schema.SchemaBasedJAXBMain;
import Engine.XMLLoading.jaxb.schema.generated.Stop;
import Engine.XMLLoading.jaxb.schema.generated.TransPool;
import Engine.XMLLoading.jaxb.schema.generated.TransPoolTrip;

import java.util.Map;

public class EngineManager {
    private static EngineManager engineManagerInstance;
    private TransPool transPool;
    private static TripRequestsUtil tripRequestUtil;
    private static TripSuggestUtil tripSuggestUtil;

    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManagerInstance == null) {
            engineManagerInstance = new EngineManager();
            tripSuggestUtil = new TripSuggestUtil();
            tripRequestUtil = new TripRequestsUtil();
        }
        return engineManagerInstance;
    }

    public String LoadXML() {
        SchemaBasedJAXBMain jax = new SchemaBasedJAXBMain();
        transPool = jax.init();

        XMLValidationsImpl xmlValidator = new XMLValidationsImpl();
//        if (xmlValidator.validateXmlFile(null)) {
//            return xmlValidator.getValidMessage();
//        } else {
//            return xmlValidator.getErrorMessage();
//        }
        return "OK";
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
            str.append(String.format("Trip ID - %d\n", getSuggestTripID(trip)));
            str.append(String.format("Trip Owner - %s\n", trip.getOwner()));
            str.append(String.format("Trip Route - %s\n", trip.getRoute().getPath()));
            str.append(String.format("Trip Price - %s\n", trip.getPPK())); //have to fix
            str.append(String.format("Trip starting hour - %s, Trip arrival hour - %s\n", trip.getScheduling().getHourStart(), trip.getScheduling().getDayStart())); // have to fix
            str.append(String.format("Trip available sits - %s\n", trip.getCapacity())); // have to fix
            str.append(String.format("Exists passengers trip - %s\n", null));// have to fix
            str.append(String.format("Trip ID - %s\n", null));
            str.append(String.format("Trip ID - %s\n", null));
        }
        return str.toString();
    }

    private Integer getSuggestTripID(TransPoolTrip trip) {
        return tripSuggestUtil.getTripID(trip);
    }

    public String getAllTripRequests () {
        StringBuilder str = new StringBuilder();
        str.append("All requested trips:\n");

        for(Map.Entry<TripRequest, Integer> trip : tripRequestUtil.getRequestTrips().entrySet()) {
            str.append(String.format("Trip ID - %d", getRequestTripID(trip.getKey())));
            str.append(String.format("Trip requester - %s", trip.getKey().getNameOfOwner()));
            str.append(String.format("Trip source station - %s Trip destination station - %s", trip.getKey().getSourceStation(), trip.getKey().getDestinationStation()));
            str.append(String.format("Trip starting hour - %d", trip.getKey().getStartingHour()));

            if(trip.getKey().isMatched()) {
                str.append(String.format("Trip Match ID - %d", null)); // have to fix
                str.append(String.format("Trip Match ID - %d", null));
                str.append(String.format("Trip Match ID - %d", null));
                str.append(String.format("Trip Match ID - %d", null));
                str.append(String.format("Trip Match ID - %d", null));
            }
        }
        return str.toString();
    }

    private Integer getRequestTripID(TripRequest trip) {
        return tripRequestUtil.getTripID(trip);
    }

    public void addNewTripRequest(String input) {
        String[] inputs = input.split(",");
        TripRequest newRequest = new TripRequest(inputs[0], inputs[1], inputs[2], Integer.parseInt(inputs[3]));
        tripRequestUtil.addRequestTrip(newRequest);
    }


    public boolean validateTripRequestInput(String input) {
        return tripRequestUtil.validateTripRequestInput(input);
    }

    public String getRequestValidationErrorMessage () {
        return tripRequestUtil.getValidationErrorMessage();
    }

    public String getRequestValidationSuccessMessage () {
        return tripRequestUtil.getValidationSuccessMessage();
    }

    public TransPool getTransPool() {
        return transPool;
    }
}

