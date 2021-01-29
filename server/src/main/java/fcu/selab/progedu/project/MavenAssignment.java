package fcu.selab.progedu.project;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import fcu.selab.progedu.jenkinsconfig.JenkinsProjectConfig;
import fcu.selab.progedu.jenkinsconfig.MavenConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class MavenAssignment extends ProjectType {
  private static final Logger LOGGER = LoggerFactory.getLogger(MavenAssignment.class);

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.MAVEN;
  }

  @Override
  public String getSampleTemplate() {
    return "MavenQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigPath() {
    URL url = this.getClass().getResource("/jenkins/config_maven.xml");
    return url.getPath();
  }

  @Override
  public void createJenkinsJobConfig(String username, String projectName) {
    try {

      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
          + ".git";

      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
              + "/webapi";
      String updateDbUrl = progEduApiUrl + "/commits/update";

      JenkinsProjectConfig jenkinsProjectConfig = new MavenConfig(projectUrl, updateDbUrl,
                                                                  username, projectName);

      String jenkinsJobConfigPath = getJenkinsJobConfigPath();
      Path jenkinsConfigPath = Paths.get(jenkinsJobConfigPath);
      jenkinsProjectConfig.writeToFile(jenkinsConfigPath);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
      } else if (statusService.isMavenCompileFailureOfUnitTest(console)) {
        status = StatusEnum.COMPILE_FAILURE_OF_UNIT_TEST;
      } else {
        status = StatusEnum.COMPILE_FAILURE;
      }
    }
    return status;
  }

}
