package fcu.selab.progedu.project;

import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.status.StatusFactory;
import fcu.selab.progedu.status.StatusFactorySelector;

public abstract class ProjectType {

  public Status getStatus(String statusType) {
    StatusFactory statusFactory = StatusFactorySelector.getStatusFactory(getProjectType());
    return statusFactory.getStatus(statusType);
  }

  public abstract ProjectTypeEnum getProjectType();

  public abstract StatusEnum checkStatusType(int num, String username, String assignmentName);

}
