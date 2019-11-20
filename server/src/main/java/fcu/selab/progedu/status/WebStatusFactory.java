package fcu.selab.progedu.status;

public class WebStatusFactory implements StatusFactory {

  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case CHECKSTYLE_FAILURE: {
        return new WebCheckstyleFailure();
      }
      case WEB_HTMLHINT_FAILURE: {
        return new WebHtmlhintFailure();
      }
      case WEB_STYLELINT_FAILURE: {
        return new WebStylelintFailure();
      }
      case WEB_ESLINT_FAILURE: {
        return new WebEslintFailure();
      }
      case UNIT_TEST_FAILURE: {
        return new WebUnitTestFailure();
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
