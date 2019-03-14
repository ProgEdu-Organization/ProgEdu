package fcu.selab.progedu.status;

public class WebUnitTestFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo;
    String unitTestFailureStart = "+ npm run test";
    String unitTestFailureEnd = "(Session info:";


    return "123";
  }

}
