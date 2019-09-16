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
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.data.ZipFileInfo;
import fcu.selab.progedu.utils.ZipHandler;

public class MavenAssignment extends AssignmentType {

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.MAVEN;
  }

  @Override
  public String getSampleTemplate() {
    return "MavenQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigSample() {
    return "config_maven.xml";
  }

  @Override
  public void createJenkinsJobConfig(String username, String projectName) {
    try {
      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
      String jenkinsJobConfigPath = this.getClass()
          .getResource("/jenkins/" + getJenkinsJobConfigSample()).getPath();

      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
          + "/webapi";
      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
          + ".git";
      String updateDbUrl = progEduApiUrl + "/commits/update";
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
      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
      doc.getElementsByTagName("testFileUrl").item(0).setTextContent(testFileUrl);
      doc.getElementsByTagName("user").item(0).setTextContent(username);
      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);

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
  public StatusEnum checkStatusType(int num, String username, String assignmentName) {
    StatusEnum status;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(num)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String jobName = username + "_" + assignmentName;
      String console = jenkinsService.getConsole(jobName, num);

      if (statusService.isBuildSuccess(console)) {
        status = StatusEnum.BUILD_SUCCESS;
      } else if (statusService.isMavenUnitTestFailure(console)) {
        status = StatusEnum.UNIT_TEST_FAILURE;
      } else if (statusService.isMavenCheckstyleFailure(console)) {
        status = StatusEnum.CHECKSTYLE_FAILURE;
      } else {
        status = StatusEnum.COMPILE_FAILURE;
      }
    }
    return status;
  }

  @Override
  public void createTemplate(String cloneDirectoryPath) {
    try {
      FileUtils.deleteDirectory(new File(cloneDirectoryPath + "/src/test"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ZipFileInfo createTestCase(String testDirectory) {
    ZipHandler zipHandler;
    ZipFileInfo zipFileInfo = null;

    try {
      FileUtils.deleteDirectory(new File(testDirectory + "/src/main"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      zipHandler = new ZipHandler();
      zipFileInfo = zipHandler.getZipInfo(testDirectory);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    return zipFileInfo;
  }

}
