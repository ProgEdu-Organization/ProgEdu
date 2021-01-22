package fcu.selab.progedu.project;

import fcu.selab.progedu.status.StatusEnum;

public abstract class GroupProjectType extends ProjectType {

  @Override
  public StatusEnum checkStatusType(int num, String username, String assignmentName) {
    ProjectType projectType = ProjectTypeFactory
        .getProjectType(getProjectType().getTypeName());
    return projectType.checkStatusType(num, username, assignmentName);
  }
}
