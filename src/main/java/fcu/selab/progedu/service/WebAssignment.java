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

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.status.WebStatusFactory;

public class WebAssignment extends AssignmentTypeMethod {

  public WebAssignment() {
    super(new WebStatusFactory());
  }

  public String getSampleZip() {
    return "WebQuickStart.zip";
  }

  /**
   * extract main method and modify pom.xml
   * 
   * @param testDirectory testDirectory
   * @param projectName   projectName
   */
  public void extractFile(String zipFilePath, String testDirectory, String destDirectory,
      String projectName) {

    try {
      FileUtils.deleteDirectory(new File(testDirectory + "/src/web"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      FileUtils.deleteDirectory(new File(destDirectory + "/src/test"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getJenkinsConfig() {
    return "config_web.xml";
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

      String strJobName = userName + "_" + proName;
      Node jobName = doc.getElementsByTagName("jobName").item(0);
      jobName.setTextContent(strJobName);

      Node testFileName = doc.getElementsByTagName("testFileName").item(0);
      testFileName.setTextContent(proName);

      Node proDetailUrl = doc.getElementsByTagName("proDetailUrl").item(0);
      proDetailUrl.setTextContent(tomcatUrl);

      JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
      String seleniumUrl = jenkinsData.getSeleniumHostUrl() + "/wd/hub";
      Node ndSeleniumUrl = doc.getElementsByTagName("seleniumUrl").item(0);
      ndSeleniumUrl.setTextContent(seleniumUrl);

      String updateDbUrl = progApiUrl + "/commits/update";
      Node progeduDbUrl = doc.getElementsByTagName("progeduDbUrl").item(0);
      progeduDbUrl.setTextContent(updateDbUrl);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName("proName").item(0);
      ndProName.setTextContent(proName);

      String progeduApiUrl = progApiUrl;
      Node ndProgeduApiUrl = doc.getElementsByTagName("progeduAPIUrl").item(0);
      ndProgeduApiUrl.setTextContent(progeduApiUrl);

      Node jenkinsJobName = doc.getElementsByTagName("jenkinsJobName").item(0);
      jenkinsJobName.setTextContent(strJobName);
      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException
        | LoadConfigFailureException e) {
      e.printStackTrace();
    }

  }
}
