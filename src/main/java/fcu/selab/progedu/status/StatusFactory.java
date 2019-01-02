package fcu.selab.progedu.status;

public class StatusFactory {
  /**
   * 
   * @param type type
   * @return Status status
   */
  public static Status getStatus(String type) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(type);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case COMPILE_FAILURE: {
        return new CompileFailure();
      }
      case CHECKSTYLE_FAILURE: {
        return new CheckstyleFailure();
      }
      case UNIT_TEST_FAILURE: {
        return new UnitTestFailure();
      }
      case BUILD_SUCCESS: {
        return new BuildSuccess();
      }

      default: {
        return null;
      }

    }
  }
}
