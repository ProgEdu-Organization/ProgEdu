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

public class JavacConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(JavacConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/config_javac.xml");
  Document xmlDocument;

  /**
   * MavenConfig
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl  updateDbUrl
   * @param username     username
   * @param projectName  projectName
   */
  public JavacConfig(String projectUrl, String updateDbUrl,
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

      // Todo 這目前無效, 已經沒有 test資料夾了
//    String assignmentPath = System.getProperty("java.io.tmpdir") + "/tests/" + projectName;
//    String command = getCommandFromFile(assignmentPath); // Todo 需參考 JavacAssignment.bak
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

//  private void setProgEduJavacCommand(String command) { // Todo
//    this.xmlDocument.getElementsByTagName("command").item(0).setTextContent(command);
//  }

//  /**
//   * Raed the command file in test directory and return command string
//   *
//   * @param assignmentPath assignmentPath
//   */
//  private String getCommandFromFile(String assignmentPath) { // Todo 這是要去cloneDirectoryPath 找資料
////    StringBuilder sb = new StringBuilder();
////
////    try (BufferedReader br = Files.newBufferedReader(Paths.get(assignmentPath + "-command"))) {
////      String line;
////
////      while ((line = br.readLine()) != null) {
////        sb.append(line).append("\n");
////      }
////    } catch (IOException e) {
////      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
////      LOGGER.error(e.getMessage());
////    }
////    return sb.toString();
//  }

}
