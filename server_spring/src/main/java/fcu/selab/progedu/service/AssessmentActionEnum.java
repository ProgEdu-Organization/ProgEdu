package fcu.selab.progedu.service;

public enum AssessmentActionEnum {
  DO("do"), REVIEW("review");

  private String action;

  private AssessmentActionEnum(String action) {
    this.action = action;
  }

  /**
   * get assessment action enum.
   *
   * @param action action
   * @return assessment action enum
   */
  public static AssessmentActionEnum getActionEnum(String action) {
    for (AssessmentActionEnum assessmentActionEnum : AssessmentActionEnum.values()) {
      if(assessmentActionEnum.getTypeName().equals(action)) {
        return assessmentActionEnum;
      }
    }
    return null;
  }

  public String getTypeName() {
    return this.action;
  }
}
