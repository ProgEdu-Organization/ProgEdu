package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;

public class JavaJenkinsJobStatus extends JenkinsJobStatus {

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
      } else {
        status = StatusEnum.COMPILE_FAILURE;
      }
    }
    return status;
  }

}
