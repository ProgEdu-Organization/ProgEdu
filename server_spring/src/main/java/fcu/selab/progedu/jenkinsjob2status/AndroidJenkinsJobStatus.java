package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;

public class AndroidJenkinsJobStatus extends JenkinsJobStatus {


  @Override
  public StatusEnum getStatus(String jenkinsJobName, int buildCount) {
    StatusEnum status;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(buildCount)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String console = jenkinsService.getConsole(jenkinsJobName, buildCount);

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
