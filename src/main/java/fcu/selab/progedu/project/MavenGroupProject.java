package fcu.selab.progedu.project;

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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class MavenGroupProject extends GroupProject {

  @Override
  public String getJenkinsConfig() {
    return "group_maven_config.xml";
  }

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.MAVEN;
  }

  @Override
  /**
   * modify Jenkins job configuration base on sample file
   * 
   * @param filePath    filePath
   * @param updateDbUrl updateDbUrl
   * @param userName    userName
   * @param proName     proName
   * @param tomcatUrl   tomcatUrl
   */
  public void createJenkinsJobConfiguration(String filePath, String updateDbUrl, String userName,
      String proName, String tomcatUrl) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

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
