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
            MavenConfig mavenConfig = new MavenConfig("unit-url", "unit-db-url", "unit-username", "unit-project");
            String testOut = mavenConfig.getXmlConfig();
            System.out.printf(testOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getXMLDocument() {
        try {
            MavenConfig mavenConfig = new MavenConfig("unit-url", "unit-db-url", "unit-username", "unit-project");
            Document doc = mavenConfig.getXmlDocument();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void setAll() {

        try {
            MavenConfig mavenConfig = new MavenConfig("unit-url", "unit-db-url", "unit-username", "unit-project");
            System.out.printf(mavenConfig.getXmlConfig());

            Path jenkinsConfigPath = Paths.get("./config_maven-test.xml");
            System.out.printf(jenkinsConfigPath.toString());
            mavenConfig.writeToFile(jenkinsConfigPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}