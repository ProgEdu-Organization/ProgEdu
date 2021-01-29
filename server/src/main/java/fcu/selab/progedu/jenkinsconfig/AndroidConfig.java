package fcu.selab.progedu.jenkinsconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AndroidConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/config_maven.xml");
  Document xmlDocument;

  /**
   * MavenConfig
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl  updateDbUrl
   * @param username     username
   * @param projectName  projectName
   */
  public AndroidConfig(String projectUrl, String updateDbUrl,
                       String username, String projectName) throws Exception {

    Path basePath = Paths.get(this.baseUrl.toURI());
    File baseFile = basePath.toFile();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
    DocumentBuilder builder = factory.newDocumentBuilder();
    this.xmlDocument = builder.parse(baseFile);

    setGitLabProjectUrl(projectUrl);
    setProgEduUpdateUrl(updateDbUrl);
    setProgEduUpdateUsername(username);
    setProgEduUpdateProjectName(projectName);

  }

  @Override
  public Document getXmlDocument() {
    return this.xmlDocument;
  }

  private void setGitLabProjectUrl(String projectUrl) {
    this.xmlDocument.getElementsByTagName("url").item(0).setTextContent(projectUrl);
  }

  private void setProgEduUpdateUrl(String updateDbUrl) {
    this.xmlDocument.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
  }

  private void setProgEduUpdateUsername(String username) {
    this.xmlDocument.getElementsByTagName("user").item(0).setTextContent(username);
  }

  private void setProgEduUpdateProjectName(String projectName) {
    this.xmlDocument.getElementsByTagName("proName").item(0).setTextContent(projectName);
  }

}
