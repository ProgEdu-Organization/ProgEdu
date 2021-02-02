package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;

public class MavenJenkinsJobStatus extends JenkinsJobStatus {


  @Override
  public StatusEnum getStatus(String jenkinsJobName, int buildCount) {
    StatusEnum status;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(buildCount)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String console = jenkinsService.getConsole(jenkinsJobName, buildCount);

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
