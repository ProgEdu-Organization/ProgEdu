package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PythonPipelineConfig extends JenkinsProjectConfig{
  private static final Logger LOGGER = LoggerFactory.getLogger(PythonPipelineConfig.class);

  InputStream baseConfig = this.getClass().getResourceAsStream("/jenkins/pipelineConfig.xml");
  Document xmlDocument;

  /**
   * AndroidConfig
   *
   * @param projectUrl   projectUrl
   */
  public PythonPipelineConfig(String projectUrl, String updateDbUrl,
                               String username, String projectName) {

    try {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseConfig);

      setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

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
      InputStream androidPipeline = this.getClass().getResourceAsStream("/jenkins/python-pipeline");

      String pipeLine = IOUtils.toString(androidPipeline, StandardCharsets.UTF_8);
      pipeLine = pipeLine.replaceAll("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-project-name\\}", projectName);

      newPipeLine = pipeLine;

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return newPipeLine;
  }

  @Override
  public Document getXmlDocument() {
    return this.xmlDocument;
  }
}
