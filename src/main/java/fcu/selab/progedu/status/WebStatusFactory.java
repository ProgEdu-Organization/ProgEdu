package fcu.selab.progedu.status;

public class WebStatusFactory extends StatusFactory {

  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case UNIT_TEST_FAILURE: {
        return new WebUnitTestFailure();
      }
      case CHECKSTYLE_FAILURE: {
        return new WebCheckstyleFailure();
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
