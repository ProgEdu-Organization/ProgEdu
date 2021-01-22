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

import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class AndroidAssignment extends ProjectType {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebAssignment.class);

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.ANDROID;
  }

  @Override
  public String getSampleTemplate() {
    return "AndroidQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigSample() {
    return "config_android.xml";
  }

  @Override
  public void createJenkinsJobConfig(String username, String projectName) {
    try {
      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
      String jenkinsJobConfigPath =
          this.getClass().getResource("/jenkins/" + getJenkinsJobConfigSample()).getPath();

      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl =
          courseConfig.getTomcatServerIp() + courseConfig.getBaseuri() + "/webapi";
      String projectUrl =
          gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName + ".git";
      String updateDbUrl = progEduApiUrl + "/commits/update";
      JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
      String checksumUrl = progEduApiUrl + "/assignment/checksum?proName=" + projectName;
      String testFileUrl = AssignmentDbManager.getInstance().getTestFileUrl(projectName);
      String stringEmpty = "";

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(jenkinsJobConfigPath);

      String jobName = username + "_" + projectName;
      // config_android.xml
      // UserRemoteConfig
      doc.getElementsByTagName("url").item(0).setTextContent(projectUrl);

      // Test
      doc.getElementsByTagName("jobName").item(0).setTextContent(jobName);
      doc.getElementsByTagName("testFileName").item(0).setTextContent(projectName);
      doc.getElementsByTagName("proDetailUrl").item(0).setTextContent(checksumUrl);
      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
      doc.getElementsByTagName("testFileUrl").item(0).setTextContent(testFileUrl);

      // UpdatingDbPublisher
      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
      doc.getElementsByTagName("user").item(0).setTextContent(username);
      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);
      doc.getElementsByTagName("progeduAPIUrl").item(0).setTextContent(progEduApiUrl);
      doc.getElementsByTagName("jenkinsUsername").item(0).setTextContent(username);
      doc.getElementsByTagName("jenkinsAssignmentName").item(0).setTextContent(projectName);

      // GitLabPushTrigger
      doc.getElementsByTagName("secretToken").item(0).setTextContent(stringEmpty);

      // AndroidEmulator
      doc.getElementsByTagName("avdNameSuffix").item(0).setTextContent(jobName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(jenkinsJobConfigPath));
      transformer.transform(source, result);
    } catch (LoadConfigFailureException
        | ParserConfigurationException
        | SAXException
        | IOException
        | TransformerException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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

      if (statusService.isAndroidCompileFailure(console)) {
        status = StatusEnum.COMPILE_FAILURE;
      } else if (statusService.isAndroidLintFailure(console)) {
        status = StatusEnum.ANDROID_LINT_FAILURE;
      } else if (statusService.isAndroidCheckstyleFailure(console)) {
        status = StatusEnum.CHECKSTYLE_FAILURE;
      } else if (statusService.isAndroidUnitTestFailure(console)) {
        status = StatusEnum.UNIT_TEST_FAILURE;
      } else if (statusService.isAndroidUiTestFailure(console)) {
        status = StatusEnum.UI_TEST_FAILURE;
      } else {
        status = StatusEnum.BUILD_SUCCESS;
      }
    }
    return status;
  }
}
