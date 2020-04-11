package Engine.XMLLoading.jaxb.schema;

import Engine.XMLLoading.jaxb.schema.generated.TransPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class SchemaBasedJAXBMain {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.XMLLoading.jaxb.schema.generated";

    public TransPool init() {
        InputStream inputStream = SchemaBasedJAXBMain.class.getResourceAsStream("/resources/master.xml");
        try {
            return deserializeFrom(inputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println("x");
        return null;

    }
    private TransPool deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        TransPool transPool = (TransPool) u.unmarshal(in);
        System.out.println(transPool);
        return transPool;
    }
}
