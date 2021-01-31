package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.config.CourseConfig;
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

public class AndroidConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/config_android.xml");
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

      String jobName = username + "_" + projectName;
      this.xmlDocument.getElementsByTagName("jobName").item(0).setTextContent(jobName);

      // AndroidEmulator
      this.xmlDocument.getElementsByTagName("avdNameSuffix").item(0).setTextContent(jobName);


      // Todo 以下三行目前會失效, 原本是要做Test的
//    doc.getElementsByTagName("testFileName").item(0).setTextContent(projectName);
//    doc.getElementsByTagName("proDetailUrl").item(0).setTextContent(checksumUrl);
//    doc.getElementsByTagName("testFileUrl").item(0).setTextContent(testFileUrl);

      // UpdatingDbPublisher
      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
              + "/webapi";
      this.xmlDocument.getElementsByTagName("progeduAPIUrl").item(0).setTextContent(progEduApiUrl);

      this.xmlDocument.getElementsByTagName("jenkinsUsername").item(0).setTextContent(username);

      this.xmlDocument.getElementsByTagName("jenkinsAssignmentName").item(0)
              .setTextContent(projectName);

      // Todo 即將砍掉以下這行
      this.xmlDocument.getElementsByTagName("secretToken").item(0).setTextContent("");

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
