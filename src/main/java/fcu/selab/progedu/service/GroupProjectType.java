package fcu.selab.progedu.service;

public enum GroupProjectType {
  JAVAC("Javac"), MAVEN("Maven"), WEB("Web"), APP("App");

  private String type;

  public String getType() {
    return type;
  }

  private GroupProjectType(String projectType) {
    this.type = projectType;
  }

  /**
   * 
   * @param type is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static GroupProjectType getGroupProjectType(String type) {
    for (GroupProjectType projectType : GroupProjectType.values()) {
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
