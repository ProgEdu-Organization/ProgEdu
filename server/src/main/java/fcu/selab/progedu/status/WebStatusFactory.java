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
//      case CHECKSTYLE_FAILURE: {              //Need to Delete in issues#153
//        return new WebCheckstyleFailure();    //Need to Delete in issues#153
//      }                                       //Need to Delete in issues#153
      case WEBHTML_FAILURE: {
        return new WebHtmlFailure();
      }
      case WEBSTYLE_FAILURE: {
        return new WebStyleFailure();
      }
      case WEBES_FAILURE: {
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
