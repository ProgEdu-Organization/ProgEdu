package fcu.selab.progedu.service;

public enum AssignmentTypeEnum {
  JAVAC("Javac"), MAVEN("Maven"), WEB("Web"), APP("App");

  private String type;

  public String getType() {
    return type;
  }

  private AssignmentTypeEnum(String projectType) {
    this.type = projectType;
  }

  /**
   * 
   * @param type is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static AssignmentTypeEnum getStatusProjectTypeEnum(String type) {
    for (AssignmentTypeEnum status : AssignmentTypeEnum.values()) {
      if (status.getTypeName().equals(type)) {
        return status;
      }
    }
    return null;
  }

  public String getTypeName() {
    return this.type;
  }
}
