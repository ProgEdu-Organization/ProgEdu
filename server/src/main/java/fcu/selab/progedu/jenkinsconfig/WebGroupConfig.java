package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebGroupConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebGroupConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/group_web_config.xml");
  Document xmlDocument;

  /**
   * WebGroupConfig
   * Todo 這好像跟 web 個人作業設定差不多, 之後可以刪減
   * @param projectUrl   projectUrl
   * @param updateDbUrl  updateDbUrl
   * @param username     username
   * @param projectName  projectName
   */
  public WebGroupConfig(String projectUrl, String updateDbUrl,
                        String username, String projectName) {

    try {
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


      JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
      String seleniumUrl = jenkinsData.getSeleniumHostUrl() + "/wd/hub";
      this.xmlDocument.getElementsByTagName("seleniumUrl").item(0).setTextContent(seleniumUrl);


      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
              + "/webapi";
      this.xmlDocument.getElementsByTagName("progeduAPIUrl").item(0).setTextContent(progEduApiUrl);


      this.xmlDocument.getElementsByTagName("jenkinsUsername").item(0).setTextContent(username);

      this.xmlDocument.getElementsByTagName("jenkinsAssignmentName").item(0)
              .setTextContent(projectName);


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
