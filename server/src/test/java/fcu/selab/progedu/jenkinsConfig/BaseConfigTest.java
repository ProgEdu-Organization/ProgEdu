package fcu.selab.progedu.jenkinsConfig;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class BaseConfigTest {

    @Test
    public void getXMLConfig() {
        try {
            BaseConfig baseConfig = new BaseConfig();
            String testOut = baseConfig.getXMLConfig();
            System.out.printf(testOut);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void getXMLDocument() {
        try {
            BaseConfig baseConfig = new BaseConfig();
            Document doc = baseConfig.getXMLDocument();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setXMLDocument() {
        try {
            BaseConfig baseConfig = new BaseConfig();
            Document doc = baseConfig.getXMLDocument();
            doc.getElementsByTagName("url").item(0).setTextContent("unit-test");

            baseConfig.setXMLDocument(doc);
            System.out.printf(baseConfig.getXMLConfig());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}