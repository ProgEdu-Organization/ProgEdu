package fcu.selab.progedu.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.conn.JenkinsService;
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
