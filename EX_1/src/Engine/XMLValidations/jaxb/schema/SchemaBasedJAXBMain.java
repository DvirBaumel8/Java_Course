package Engine.XMLValidations.jaxb.schema;


import jaxb.schema.generated.TransPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class SchemaBasedJAXBMain {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "EngineXMLValidations.jaxb.schema.generated";

    public static void main(String[] args) {
        InputStream inputStream = SchemaBasedJAXBMain.class.
                getResourceAsStream("//Users/ohadbar/transPool/Java_Course/EX_1/src/Engine/XMLValidations/resources/master.xml");
        try {
            TransPool transPool = deserializeFrom(inputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println("x");

    }
    private static TransPool deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        TransPool transPool = (TransPool) u.unmarshal(in);
        System.out.println(transPool);
        return transPool;
    }
}
