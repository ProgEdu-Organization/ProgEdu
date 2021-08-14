package fcu.selab.progedu.service;

public enum AssignmentActionEnum {
  DO("Do HW"), REVIEW("Review HW");

  private String assignmentAction;

  private AssignmentActionEnum(String assignmentAction) {
    this.assignmentAction = assignmentAction;
  }

  public static AssignmentActionEnum getAssignmentActionEnum(String assignmentAction) {
    for (AssignmentActionEnum assignmentActionType : AssignmentActionEnum.values()) {
      if (assignmentActionType.getActionName().equals(assignmentAction)) {
        return assignmentActionType;
      }
    }
    return null;
  }

  public String getActionName() {
    return this.assignmentAction;
  }
}
