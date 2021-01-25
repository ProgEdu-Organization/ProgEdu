package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;
import org.w3c.dom.Document;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MavenConfigTest {

    @Test
    public void getXMLConfig() {
        try {
            MavenConfig mavenConfig = new MavenConfig();
            String testOut = mavenConfig.getXmlConfig();
            System.out.printf(testOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getXMLDocument() {
        try {
            MavenConfig mavenConfig = new MavenConfig();
            Document doc = mavenConfig.getXmlDocument();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setXMLDocument() {
        try {
            MavenConfig mavenConfig = new MavenConfig();
            Document doc = mavenConfig.getXmlDocument();
            doc.getElementsByTagName("url").item(0).setTextContent("unit-test");

            mavenConfig.setXmlDocument(doc);
            System.out.printf(mavenConfig.getXmlConfig());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setAll() {

        try {
            MavenConfig mavenConfig = new MavenConfig();
            mavenConfig.setAll("unit-url", "unit-db-url", "unit-username", "unit-project");
            System.out.printf(mavenConfig.getXmlConfig());

            Path absolutePath = Paths.get("E:\\franky-test.xml");
            mavenConfig.writeToFile(absolutePath);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}