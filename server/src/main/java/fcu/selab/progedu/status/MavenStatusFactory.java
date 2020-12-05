package fcu.selab.progedu.status;

public class MavenStatusFactory implements StatusFactory {
  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case COMPILE_FAILURE: {
        return new MavenCompileFailure();
      }
      case UNIT_TEST_FAILURE: {
        return new MavenUnitTestFailure();
      }
      case CHECKSTYLE_FAILURE: {
        return new MavenCheckstyleFailure();
      }
      case BUILD_SUCCESS: {
        return new BuildSuccess();
      }
      case COMPILE_FAILURE_OF_UNIT_TEST: {
        return new MavenCompileFailureOfUnitTest();
      }
      default: {
        return null;
      }
    }
  }
}
