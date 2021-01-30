package fcu.selab.progedu.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;

public class WebAssignment extends ProjectType {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebAssignment.class);

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.WEB;
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
      } else if (statusService.isWebHtmlhintFailure(console)) {
        status = StatusEnum.WEB_HTMLHINT_FAILURE;
      } else if (statusService.isWebStylelintFailure(console)) {
        status = StatusEnum.WEB_STYLELINT_FAILURE;
      } else if (statusService.isWebEslintFailure(console)) {
        status = StatusEnum.WEB_ESLINT_FAILURE;
      } else {
        status = StatusEnum.BUILD_SUCCESS;
      }
    }
    return status;
  }
}
