package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.status.StatusEnum;

public class PythonJenkinsJobStatus extends JenkinsJobStatus {
  @Override
  public StatusEnum getStatus(String jenkinsJobName, int buildCount) {
    StatusEnum status;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(buildCount)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String console = jenkinsService.getConsole(jenkinsJobName, buildCount);

      if (statusService.isPythonCompileFailure(console)) {
        status = StatusEnum.COMPILE_FAILURE;
      } else if (statusService.isPythonUnitTestFailure(console)) {
        status = StatusEnum.UNIT_TEST_FAILURE;
      } else if (statusService.isPythonCheckstyleFailure(console)) {
        status = StatusEnum.CHECKSTYLE_FAILURE;
      } else {
        status = StatusEnum.BUILD_SUCCESS;
      }
    }
    return status;
  }
}
