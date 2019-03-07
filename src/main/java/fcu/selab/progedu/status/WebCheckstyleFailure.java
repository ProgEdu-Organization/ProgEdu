package fcu.selab.progedu.status;

public class WebCheckstyleFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo;
    String checkstyleStart = "Successfully parsed";
    String checkstyleEnd = "duplicates";
    
    checkstyleInfo = consoleText.substring(consoleText.indexOf(checkstyleStart),
        consoleText.indexOf(checkstyleEnd) + checkstyleEnd.length());
    return checkstyleInfo;
  }
}
