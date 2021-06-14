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

public class WebPipelineConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebPipelineConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/pipelineConfig.xml");
  Document xmlDocument;

  /**
   * WebPipelineConfig
   *
   * @param projectUrl   projectUrl
   */
  public WebPipelineConfig(String projectUrl, String updateDbUrl,
                             String username, String projectName, String updateScreenShotDb) {

    try {
      Path basePath = Paths.get(this.baseUrl.toURI());
      File baseFile = basePath.toFile();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseFile);

      setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName, updateScreenShotDb);

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
                                  String username, String projectName, String updateScreenShotDb) {

    String pipeline = createPipeline(projectUrl, updateDbUrl, username,
                                     projectName, updateScreenShotDb);

    this.xmlDocument.getElementsByTagName("script").item(0).setTextContent(pipeline);
  }

  /**
   * createPipeline
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl   updateDbUrl
   * @param username   username
   * @param projectName   projectName
   * @param updateScreenShotDb   updateScreenShotDb
   */
  public String createPipeline(String projectUrl, String updateDbUrl,
                               String username, String projectName, String updateScreenShotDb) {
    String newPipeLine = "";
    try {
      URL webPipelineUrl = this.getClass().getResource("/jenkins/web-pipeline");
      Path webPipelinePath = Paths.get(webPipelineUrl.toURI());
      File webPipelineFile = webPipelinePath.toFile();


      String pipeLine = JavaIoUtile.readFileToString(webPipelineFile);
      pipeLine = pipeLine.replaceAll("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-project-name\\}", projectName);

      pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-screenshot-updateDbUrl\\}",
              updateScreenShotDb);

      newPipeLine = pipeLine;

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return newPipeLine;
  }
}
