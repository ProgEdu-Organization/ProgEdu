package fcu.selab.progedu.status;

public enum StatusAssigmentTypeEnum {
  JAVA("Javac"), Maven("Maven"), WEB("Web"), APP("App");

  private String type;

  public String getType() {
    return type;
  }

  private StatusAssigmentTypeEnum(String projectType) {
    this.type = projectType;
  }

  /**
   * 
   * @param type is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static StatusAssigmentTypeEnum getStatusProjectTypeEnum(String type) {
    for (StatusAssigmentTypeEnum status : StatusAssigmentTypeEnum.values()) {
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
