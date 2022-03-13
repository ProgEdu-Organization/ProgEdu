package fcu.selab.progedu.status;

public class PythonStatusFactory implements StatusFactory {
  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }

      case COMPILE_FAILURE: {
        return new PythonCompileFailure();
      }

      case CHECKSTYLE_FAILURE: {
        return new PythonCheckStyleFailure();
      }

      case UNIT_TEST_FAILURE: {
        return new PythonUnitTestFailure();
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
