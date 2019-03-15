package fcu.selab.progedu.status;

public class WebUnitTestFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTestInfo = "WebUnitTestFailure";
    String unitTestFailureStart = "+ npm run test";
    String unitTestFailureEnd = "(Session info:";
    /*unitTestInfo = consoleText.substring(consoleText.indexOf(unitTestFailureStart) 
        + unitTestFailureStart.length(), consoleText.indexOf(unitTestFailureEnd));*/

    return unitTestInfo;
  }

}
