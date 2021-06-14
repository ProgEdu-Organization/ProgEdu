package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.status.StatusEnum;

public abstract class JenkinsJobStatus {

  public abstract StatusEnum getStatus(String jenkinsJobName, int buildCount);

}
