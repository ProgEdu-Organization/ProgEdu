package fcu.selab.progedu.setting;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.junit.Test;
import fcu.selab.progedu.setting.MavenAssignmentSetting;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.transform.OutputKeys.*;

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

      StringWriter strWtr = new StringWriter();
      StreamResult strResult = new StreamResult(strWtr);
      TransformerFactory tfac = TransformerFactory.newInstance();

      javax.xml.transform.Transformer transformer = tfac.newTransformer();
      transformer.setOutputProperty(INDENT, "yes");
      transformer.setOutputProperty(METHOD, "xml");

      transformer.setOutputProperty(
              "{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(new DOMSource(doc.getDocumentElement()),
              strResult);
      System.out.println(strResult.getWriter().toString());
      strWtr.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}