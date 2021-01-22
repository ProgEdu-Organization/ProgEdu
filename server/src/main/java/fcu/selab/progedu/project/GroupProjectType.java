package fcu.selab.progedu.project;

import fcu.selab.progedu.status.StatusEnum;

public abstract class GroupProjectType extends ProjectType {

  @Override
  public StatusEnum checkStatusType(int num, String username, String assignmentName) {
    AssignmentType assignmentType = AssignmentFactory
        .getAssignmentType(getProjectType().getTypeName());
    return assignmentType.checkStatusType(num, username, assignmentName);
  }
}
