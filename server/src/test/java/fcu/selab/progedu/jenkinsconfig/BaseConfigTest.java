package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;
import org.w3c.dom.Document;

public class BaseConfigTest {

    @Test
    public void getXMLConfig() {
        try {
            BaseConfig baseConfig = new BaseConfig();
            String testOut = baseConfig.getXmlConfig();
            System.out.printf(testOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getXMLDocument() {
        try {
            BaseConfig baseConfig = new BaseConfig();
            Document doc = baseConfig.getXmlDocument();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setXMLDocument() {
        try {
            BaseConfig baseConfig = new BaseConfig();
            Document doc = baseConfig.getXmlDocument();
            doc.getElementsByTagName("url").item(0).setTextContent("unit-test");

            baseConfig.setXmlDocument(doc);
            System.out.printf(baseConfig.getXmlConfig());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}