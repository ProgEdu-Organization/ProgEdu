package fcu.selab.progedu.status;

public enum StatusEnum {
  BUILD_SUCCESS("bs"), CHECKSTYLE_FAILURE("csf"), COMPILE_FAILURE("cpf"), INITIALIZATION("ini"),
  UNIT_TEST_FAILURE("utf");

  private String type;

  private StatusEnum(String type) {
    this.type = type;
  }

  /**
   * 
   * @param type is status String
   * @return status is statusEnum object
   */
  public static StatusEnum getStatusEnum(String type) {
    for (StatusEnum status : StatusEnum.values()) {
      if (status.getType().equals(type)) {
        return status;
      }
    }
    return null;
  }

  public String getType() {
    return this.type;
  }

}
