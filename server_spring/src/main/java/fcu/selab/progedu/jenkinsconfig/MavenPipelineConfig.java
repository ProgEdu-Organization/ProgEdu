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

public class MavenPipelineConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenPipelineConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/pipelineConfig.xml");
  Document xmlDocument;

  /**
   * MavenConfig
   *
   * @param projectUrl   projectUrl
   */
  public MavenPipelineConfig(String projectUrl, String updateDbUrl,
                             String username, String projectName) {

    try {
      Path basePath = Paths.get(this.baseUrl.toURI());
      File baseFile = basePath.toFile();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseFile);

      setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  @Override
  public Document getXmlDocument() {
    return this.xmlDocument;
  }


  private void setJenkinsPipeline(String projectUrl, String updateDbUrl,
                                  String username, String projectName) {

    String pipeline = createPipeline(projectUrl, updateDbUrl, username, projectName);
    this.xmlDocument.getElementsByTagName("script").item(0).setTextContent(pipeline);
  }

  /**
   * createPipeline
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl   updateDbUrl
   * @param username   username
   * @param projectName   projectName
   */
  public String createPipeline(String projectUrl, String updateDbUrl,
                               String username, String projectName) {
    String newPipeLine = "";
    try {
      URL mavenPipelineUrl = this.getClass().getResource("/jenkins/maven-pipeline");
      Path mavenPipelinePath = Paths.get(mavenPipelineUrl.toURI());
      File mavenPipelineFile = mavenPipelinePath.toFile();


      String pipeLine = JavaIoUtile.readFileToString(mavenPipelineFile);
      pipeLine = pipeLine.replaceFirst("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-project-name\\}", projectName);

      newPipeLine = pipeLine;

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return newPipeLine;
  }
}
