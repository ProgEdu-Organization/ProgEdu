package fcu.selab.progedu.setting;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.junit.Test;
import fcu.selab.progedu.setting.MavenAssignmentSetting;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MavenAssignmentSettingTest extends TestCase {

  @Test
  public void testCreateSetting() {
    String saveSettingPath = System.getProperty("java.io.tmpdir")
        + "/mavenPomXmlSetting/";

    List<String> orderList = new ArrayList<>();
    orderList.add("Compile Failure");
    orderList.add("Coding Style Failure");
    orderList.add("Unit Test Failure");

    MavenAssignmentSetting mas = new MavenAssignmentSetting();
    mas.createAssignmentSetting(orderList,
        "unit-test-for-create-maven-setting", saveSettingPath);

    try {
      File inputFile = new File(saveSettingPath
          + "unit-test-for-create-maven-setting_pom.xml");
      DocumentBuilderFactory docFactory =
          DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder =
          docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();

      System.out.println(doc);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}