package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.conn.JenkinsService;

import fcu.selab.progedu.status.StatusEnum;

public class WebJenkinsJobStatus extends JenkinsJobStatus {

  @Override
  public StatusEnum getStatus(String jenkinsJobName, int buildCount) {
    StatusEnum status;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(buildCount)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String console = jenkinsService.getConsole(jenkinsJobName, buildCount);

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
