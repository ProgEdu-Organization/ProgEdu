package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.utils.JavaIoUtile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.*;

public class MavenDindConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenDindConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/config_maven_dind.xml");
  Document xmlDocument;

  /**
   * MavenConfig
   *
   * @param projectUrl   projectUrl
   */
  public MavenDindConfig(String projectUrl) {

    try {
      Path basePath = Paths.get(this.baseUrl.toURI());
      File baseFile = basePath.toFile();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseFile);

      setGitLabProjectUrl(projectUrl);
//      setProgEduUpdateUrl(updateDbUrl);
//      setProgEduUpdateUsername(username);
//      setProgEduUpdateProjectName(projectName);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  @Override
  public Document getXmlDocument() {
    return this.xmlDocument;
  }

  private void setGitLabProjectUrl(String projectUrl) {
    this.xmlDocument.getElementsByTagName("url").item(0).setTextContent(projectUrl);
  }

  public void readPipeline() {
    try {
      URL baseUrl = this.getClass().getResource("/jenkins/maven-pipeline");
      Path basePath = Paths.get(baseUrl.toURI());
      File baseFile = basePath.toFile();
      System.out.println(JavaIoUtile.readFileToString(baseFile));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
