package fcu.selab.progedu.jenkinsconfig;

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

public class MavenGroupConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenGroupConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/group_web_config.xml");
  Document xmlDocument;

  /**
   * WebGroupConfig
   * Todo 這類別目前無功能 且壞掉 待修
   * @param projectUrl   projectUrl
   * @param updateDbUrl  updateDbUrl
   * @param username     username
   * @param projectName  projectName
   */
  public MavenGroupConfig(String projectUrl, String updateDbUrl,
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

      // Todo 需參考以下程式, 原本的作業設定是用以下過程生成的
      // Todo 是從原本的 project/MavenGroupProject.java 複製過來的
//  public void createJenkinsJobConfig(String username, String projectName) {
//    try {
//      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
//      String jenkinsJobConfigPath = getJenkinsJobConfigPath();
//
//      CourseConfig courseConfig = CourseConfig.getInstance();
//      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
//          + "/webapi/groups";
//      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
//          + ".git";
//      String updateDbUrl = progEduApiUrl + "/commits/update";
//      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//      docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
//      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//      Document doc = docBuilder.parse(jenkinsJobConfigPath);
//
//      doc.getElementsByTagName("url").item(0).setTextContent(projectUrl);
//      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
//      doc.getElementsByTagName("user").item(0).setTextContent(username);
//      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);
//
//      // write the content into xml file
//      TransformerFactory transformerFactory = TransformerFactory.newInstance();
//      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//      Transformer transformer = transformerFactory.newTransformer();
//      DOMSource source = new DOMSource(doc);
//      StreamResult result = new StreamResult(new File(jenkinsJobConfigPath));
//      transformer.transform(source, result);
//    } catch (LoadConfigFailureException | ParserConfigurationException |
//    SAXException | IOException
//        | TransformerException e) {
//      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
//      LOGGER.error(e.getMessage());
//    }
//
//  }



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
