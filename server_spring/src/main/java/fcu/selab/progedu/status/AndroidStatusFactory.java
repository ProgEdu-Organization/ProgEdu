package fcu.selab.progedu.status;

public class AndroidStatusFactory implements StatusFactory {
  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case ANDROID_LINT_FAILURE: {
        return new AndroidLintFailure();
      }
      case CHECKSTYLE_FAILURE: {
        return new AndroidCheckstyleFailure();
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
      case BUILD_SUCCESS: {
        return new BuildSuccess();
      }
      default: {
        return null;
      }
    }
  }
}
