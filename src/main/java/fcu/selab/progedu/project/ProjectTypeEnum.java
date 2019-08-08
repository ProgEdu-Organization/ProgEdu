package fcu.selab.progedu.project;

public enum ProjectTypeEnum {
  JAVAC("Javac"), MAVEN("Maven"), WEB("Web"), APP("App");

  private String type;

  public String getType() {
    return type;
  }

  private ProjectTypeEnum(String projectType) {
    this.type = projectType;
  }

  /**
   * 
   * @param type is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static ProjectTypeEnum getStatusProjectTypeEnum(String type) {
    for (ProjectTypeEnum status : ProjectTypeEnum.values()) {
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
