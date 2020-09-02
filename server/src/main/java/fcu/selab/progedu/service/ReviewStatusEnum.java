package fcu.selab.progedu.service;

public enum ReviewStatusEnum {
  INIT("initialization"), UNCOMPLETED("uncompleted"), COMPLETED("completed");

  private String reviewStatus;

  private ReviewStatusEnum(String reviewStatus) {
    this.reviewStatus = reviewStatus;
  }

  /**
   * @param reviewStatus is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static ReviewStatusEnum getReviewStatusEnum(String reviewStatus) {
    for (ReviewStatusEnum reviewStatusType : ReviewStatusEnum.values()) {
      if (reviewStatusType.getTypeName().equals(reviewStatus)) {
        return reviewStatusType;
      }
    }
    return null;
  }

  public String getTypeName() {
    return this.reviewStatus;
  }
}
