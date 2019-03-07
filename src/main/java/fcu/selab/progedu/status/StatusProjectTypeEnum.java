package fcu.selab.progedu.status;

public enum StatusProjectTypeEnum {
  JAVA("Javac"), Maven("Maven"), WEB("Web"), APP("App");

  private String type;

  public String getType() {
    return type;
  }

  private StatusProjectTypeEnum(String projectType) {
    this.type = projectType;
  }

  /**
   * 
   * @param type is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static StatusProjectTypeEnum getStatusProjectTypeEnum(String type) {
    for (StatusProjectTypeEnum status : StatusProjectTypeEnum.values()) {
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
