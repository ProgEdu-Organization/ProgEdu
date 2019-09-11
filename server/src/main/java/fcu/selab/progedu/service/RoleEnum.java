package fcu.selab.progedu.service;

public enum RoleEnum {
  TEACHER("teacher"), STUDENT("student");

  private String role;

  private RoleEnum(String role) {
    this.role = role;
  }

  /**
   * 
   * @param role is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static RoleEnum getRoleEnum(String role) {
    for (RoleEnum roleType : RoleEnum.values()) {
      if (roleType.getTypeName().equals(role)) {
        return roleType;
      }
    }
    return null;
  }

  public String getTypeName() {
    return this.role;
  }

}
