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

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;

public class WebAssignment extends AssignmentType {

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.WEB;
  }

  @Override
  public String getSampleTemplate() {
    return "WebQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigSample() {
    return "config_web.xml";
  }

  @Override
  public void createJenkinsJobConfig(String username, String projectName) {
    try {
      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
      String jenkinsJobConfigPath = this.getClass()
          .getResource("/jenkins/" + getJenkinsJobConfigSample()).getPath();

      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + "/ProgEdu/webapi";
      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
          + ".git";
      String updateDbUrl = progEduApiUrl + "/commits/update";
      JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
      String seleniumUrl = jenkinsData.getSeleniumHostUrl() + "/wd/hub";
      String checksumUrl = progEduApiUrl + "/assignment/checksum?proName=" + projectName;
      String testFileUrl = AssignmentDbManager.getInstance().getTestFileUrl(projectName);

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(jenkinsJobConfigPath);

      String jobName = username + "_" + projectName;
      doc.getElementsByTagName("url").item(0).setTextContent(projectUrl);
      doc.getElementsByTagName("jobName").item(0).setTextContent(jobName);
      doc.getElementsByTagName("testFileName").item(0).setTextContent(projectName);
      doc.getElementsByTagName("proDetailUrl").item(0).setTextContent(checksumUrl);
      doc.getElementsByTagName("seleniumUrl").item(0).setTextContent(seleniumUrl);
      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
      doc.getElementsByTagName("user").item(0).setTextContent(username);
      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);
      doc.getElementsByTagName("progeduAPIUrl").item(0).setTextContent(progEduApiUrl);
      doc.getElementsByTagName("testFileUrl").item(0).setTextContent(testFileUrl);
      doc.getElementsByTagName("jenkinsUsername").item(0).setTextContent(username);
      doc.getElementsByTagName("jenkinsAssignmentName").item(0).setTextContent(projectName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(jenkinsJobConfigPath));
      transformer.transform(source, result);
    } catch (LoadConfigFailureException | ParserConfigurationException | SAXException | IOException
        | TransformerException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void createTemplate(String uploadDirectory) {
    try {
      FileUtils.deleteDirectory(new File(uploadDirectory + "/src/test"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void createTestCase(String testDirectory) {
    try {
      FileUtils.deleteDirectory(new File(testDirectory + "/src/web"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public StatusEnum checkStatusType(int num, String username, String assignmentName) {
    StatusEnum status = null;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(num)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String jobName = username + "_" + assignmentName;
      String console = jenkinsService.getConsole(jobName, num);

    if (statusService.isWebUnitTestFailure(console)) {
        status = StatusEnum.UNIT_TEST_FAILURE;
      } else if (statusService.isWebCheckstyleFailure(console)) {
        status = StatusEnum.CHECKSTYLE_FAILURE;
      } else {
        status = StatusEnum.BUILD_SUCCESS;
      }
    }
    return status;
  }
  //
  // public WebAssignment() {
  // super(new WebStatusFactory());
  // }
  //
  // public String getSampleZip() {
  // return "WebQuickStart.zip";
  // }
  //
  // /**
  // * searchFile
  // *
  // * @param entryNewName entryNewName
  // */
  // public void searchFile(String entryNewName) {
  // }
  //
  // /**
  // * extract main method and modify pom.xml
  // *
  // * @param testDirectory testDirectory
  // * @param projectName projectName
  // */
  // public void extractFile(String zipFilePath, String testDirectory, String
  // destDirectory,
  // String projectName) {
  //
  // try {
  // FileUtils.deleteDirectory(new File(testDirectory + "/src/web"));
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  //
  // try {
  // FileUtils.deleteDirectory(new File(destDirectory + "/src/test"));
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // public String getJenkinsConfig() {
  // return "config_web.xml";
  // }
  //
  // /**
  // * modifyXmlFile
  // *
  // * @param filePath filePath
  // * @param progApiUrl progApiUrl
  // * @param userName userName
  // * @param proName proName
  // * @param tomcatUrl tomcatUrl
  // * @param sb sb
  // */
  // public void modifyXmlFile(String filePath, String progApiUrl, String
  // userName, String proName,
  // String tomcatUrl, StringBuilder sb) {
  // try {
  // String filepath = filePath;
  // DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
  // DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
  // Document doc = docBuilder.parse(filepath);
  //
  // String strJobName = userName + "_" + proName;
  // Node jobName = doc.getElementsByTagName("jobName").item(0);
  // jobName.setTextContent(strJobName);
  //
  // Node testFileName = doc.getElementsByTagName("testFileName").item(0);
  // testFileName.setTextContent(proName);
  //
  // Node proDetailUrl = doc.getElementsByTagName("proDetailUrl").item(0);
  // proDetailUrl.setTextContent(tomcatUrl);
  //
  // JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  // String seleniumUrl = jenkinsData.getSeleniumHostUrl() + "/wd/hub";
  // Node ndSeleniumUrl = doc.getElementsByTagName("seleniumUrl").item(0);
  // ndSeleniumUrl.setTextContent(seleniumUrl);
  //
  // String updateDbUrl = progApiUrl + "/commits/update";
  // Node progeduDbUrl = doc.getElementsByTagName("progeduDbUrl").item(0);
  // progeduDbUrl.setTextContent(updateDbUrl);
  //
  // Node user = doc.getElementsByTagName("user").item(0);
  // user.setTextContent(userName);
  //
  // Node ndProName = doc.getElementsByTagName("proName").item(0);
  // ndProName.setTextContent(proName);
  //
  // String progeduApiUrl = progApiUrl;
  // Node ndProgeduApiUrl = doc.getElementsByTagName("progeduAPIUrl").item(0);
  // ndProgeduApiUrl.setTextContent(progeduApiUrl);
  //
  // Node jenkinsJobName = doc.getElementsByTagName("jenkinsJobName").item(0);
  // jenkinsJobName.setTextContent(strJobName);
  // // write the content into xml file
  // TransformerFactory transformerFactory = TransformerFactory.newInstance();
  // Transformer transformer = transformerFactory.newTransformer();
  // DOMSource source = new DOMSource(doc);
  // StreamResult result = new StreamResult(new File(filepath));
  // transformer.transform(source, result);
  // } catch (ParserConfigurationException | TransformerException | SAXException
  // | IOException
  // | LoadConfigFailureException e) {
  // e.printStackTrace();
  // }
  //
  // }
}
