package fcu.selab.progedu.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fcu.selab.progedu.status.JavacStatusFactory;

public class JavacAssignment extends AssignmentTypeMethod {

  public JavacAssignment() {
    super(new JavacStatusFactory());
  }

  public String getSampleZip() {
    return "JavacQuickStart.zip";
  }

  /**
   * searchFile
   * 
   * @param entryNewName entryNewName
   */
  public void searchFile(String entryNewName) {
    StringBuilder sb = new StringBuilder();
    String last = "";
    if (entryNewName.endsWith(".java")) {
      last = entryNewName.substring(entryNewName.length() - 5, entryNewName.length());
    }
    String fileName = null;
    for (int i = 0; i < entryNewName.length() - 3; i++) {
      if (entryNewName.substring(i, i + 3).equals("src")) {
        fileName = entryNewName.substring(i);
        System.out.println("Search java file fileName : " + fileName);
        if (last.equals(".java")) {
          sb.append("javac " + fileName + "\n");
          sb.append("echo \"BUILD SUCCESS\"");
          zipHandler.setStringBuilder(sb);
        }
      }
    }
  }

  /**
   * extract main method and modify pom.xml
   * 
   * @param testDirectory testDirectory
   * @param projectName   projectName
   */
  public void extractFile(String testDirectory, String destDirectory, String projectName) {
  }

  public String getJenkinsConfig() {
    return "config_javac.xml";
  }

  /**
   * modifyXmlFile
   * 
   * @param filePath   filePath
   * @param progApiUrl progApiUrl
   * @param userName   userName
   * @param proName    proName
   * @param tomcatUrl  tomcatUrl
   * @param sb         sb
   */
  public void modifyXmlFile(String filePath, String progApiUrl, String userName, String proName,
      String tomcatUrl, StringBuilder sb) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      Node ndUrl = doc.getElementsByTagName("command").item(0);
      ndUrl.setTextContent(sb.toString());

      String updateDbUrl = progApiUrl + "/commits/update";
      Node progeduDbUrl = doc.getElementsByTagName("progeduDbUrl").item(0);
      progeduDbUrl.setTextContent(updateDbUrl);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName("proName").item(0);
      ndProName.setTextContent(proName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }

  }
}
