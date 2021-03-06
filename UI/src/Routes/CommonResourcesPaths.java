package Routes;

public class CommonResourcesPaths {
    public final static String APP_FXML_LIGHT_RESOURCE = "/resources/fxml/AppFxml.fxml";
    //public final static String APP_FXML_LIGHT_RESOURCE = "/examples/advance/nested/subcomponents/app/code/app.fxml";
    public final static String TRIP_REQUEST_FXML_RESOURCE = "/resources/fxml/TripRequestFxml.fxml";
    public final static String HEADER_fXML_RESOURCE = "/resources/fxml/header.fxml";
    public final static String TRIP_SUGGEST_fXML_RESOURCE = "/resources/fxml/TripSuggestFxml.fxml";
    public final static String MATCHING_fXML_RESOURCE = "/resources/fxml/Matching.fxml";
    public final static String LIVEMAP_fXML_RESOURCE = "/resources/fxml/LiveMap.fxml";

    private CommonResourcesPaths() {
    }

    /** thread safe */
    public static CommonResourcesPaths getInstance()
    {
        return CommonResourcesPaths.SingletonHelper.instance;
    }

    private static class SingletonHelper {
        private static CommonResourcesPaths instance = new CommonResourcesPaths();
    }
}
