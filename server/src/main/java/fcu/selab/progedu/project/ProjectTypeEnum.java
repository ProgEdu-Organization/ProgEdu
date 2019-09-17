package fcu.selab.progedu.project;

public enum ProjectTypeEnum {
  JAVAC("javac"), MAVEN("maven"), WEB("web"), APP("app");

  private String type;

  private ProjectTypeEnum(String projectType) {
    this.type = projectType;
  }

  /**
   * 
   * @param type is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static ProjectTypeEnum getProjectTypeEnum(String type) {
    for (ProjectTypeEnum projectType : ProjectTypeEnum.values()) {
      if (projectType.getTypeName().equals(type)) {
        return projectType;
      }
    }
    return null;
  }

  public String getTypeName() {
    return this.type;
  }
}
