package fcu.selab.progedu.project;

import fcu.selab.progedu.status.StatusEnum;

// Todo 這類別與它的子類別將來會砍掉
public abstract class ProjectType {

  public abstract ProjectTypeEnum getProjectType();

  public abstract StatusEnum checkStatusType(int num, String username, String assignmentName);

}
