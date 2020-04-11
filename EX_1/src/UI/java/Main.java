package UI.java;

import Engine.XMLLoading.jaxb.schema.SchemaBasedJAXBMain;
import Engine.XMLLoading.jaxb.schema.generated.TransPool;

public class Main {
    public static void main(String[] args) {
        TransPoolManager transPoolManager = TransPoolManager.getTransPoolManagerInstance();
        transPoolManager.run();
    }
}