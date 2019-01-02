package fcu.selab.progedu.status;

public enum StatusEnum {
  BUILD_SUCCESS("S"), CHECKSTYLE_FAILURE("CSF"), COMPILE_FAILURE("CPF"), INITIALIZATION("NB"),
  UNIT_TEST_FAILURE("CTF");

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
