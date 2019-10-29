package fcu.selab.progedu.status;

public class WebStatusFactory implements StatusFactory {

  @Override
  public Status getStatus(String statusType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusType);
    System.out.println(statusEnum.getType());
    switch (statusEnum) {
      case INITIALIZATION: {
        return new Initialization();
      }
      case CHECKSTYLE_FAILURE: {
        return new WebCheckstyleFailure();
      }
      case WEBHTMLHINT_FAILURE: {
        return new WebHtmlFailure();
      }
      case WEBSTYLELIST_FAILURE: {
        return new WebStyleFailure();
      }
      case WEBESLIST_FAILURE: {
        return new WebEsFailure();
      }
      case UNIT_TEST_FAILURE: {
        return new WebUnitTestFailure();
      }
      case COMPILE_FAILURE: {
        return new WebCompileFailure();
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
