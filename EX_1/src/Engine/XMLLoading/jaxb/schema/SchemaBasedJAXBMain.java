package Engine.XMLLoading.jaxb.schema;

import Engine.XMLLoading.jaxb.schema.generated.TransPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.List;

public class SchemaBasedJAXBMain {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.XMLLoading.jaxb.schema.generated";
    private String errorMessage;

    public TransPool init(String pathToXMLFile) {
        InputStream inputStream = SchemaBasedJAXBMain.class.getResourceAsStream("/resources/master.xml");
        return deserializeFrom(inputStream);
    }

    private TransPool deserializeFrom(InputStream in)  {
        JAXBContext jc = null;
        TransPool transPool = null;
        try {
           jc  = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
           Unmarshaller u = jc.createUnmarshaller();
            transPool = (TransPool) u.unmarshal(in);
        }
        catch(Exception e) {
            errorMessage = String.format("Failed to init system, please supply a valid path.\n");
        }

        return transPool;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
