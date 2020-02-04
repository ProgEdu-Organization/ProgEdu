package fcu.selab.progedu.status;

public class AndroidStatusFactory implements StatusFactory {
  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case COMPILE_FAILURE: {
        return new AndroidCompileFailure();
      }
      case UNIT_TEST_FAILURE: {
        return new AndroidUnitTestFailure();
      }
      case UI_TEST_FAILURE: {
        return new AndroidUiTestFailure();
      }
      case CHECKSTYLE_FAILURE: {
        return new AndroidCheckstyleFailure();
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
