package Engine.Manager;

import Engine.XMLLoading.XMLValidationsImpl;
import Engine.XMLLoading.jaxb.schema.SchemaBasedJAXBMain;
import Engine.XMLLoading.jaxb.schema.generated.TransPool;

import java.io.File;

public class EngineManager {
    private static EngineManager engineManager;
    private TransPool transPool;
    private EngineManager() {
    }

    public static EngineManager getEngineManagerInstance() {
        if (engineManager == null) {
            engineManager = new EngineManager();
        }
        return engineManager;
    }

    public String LoadXML() {
        SchemaBasedJAXBMain jax = new SchemaBasedJAXBMain();
        transPool = jax.init();

        XMLValidationsImpl xmlValidator = new XMLValidationsImpl();
        if (xmlValidator.validateXmlFile(new File("xmlFile"))) {
            return xmlValidator.getValidMessage();
        } else {
            return xmlValidator.getErrorMessage();
        }
    }

    public String getAllStationsName () {
            return "";
    }

    public String getAllTripOffers () {
        /*
         * -TripId
         * - Name of trip offer
         * -trip track
         * -trip price
         * - trip starting and finish hour
         * - how many passengers could be part of the trip
         * - Currently passengers that partt of the trip
         * - how many fuel the trip will required.
         * */
        return "";
    }

    public String getAllTripRequests () {
        /*
         * -TripId
         * Trip offer name
         * Starting station and destination station
         * Starting hour of the trip
         *
         * if the trip already matched please add the following details:
         * -number of the rip
         * name of the trip offer
         * price of the triop
         * Estimate hour for arrival
         * amount of required fuel
         * */
        return "";
    }
}

