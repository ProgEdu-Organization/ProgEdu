package fcu.selab.progedu.status;

public class CheckstyleFailure implements Status {

  /**
   * get checkstyle information
   * 
   * @param consoleText console text
   * @return checkstyle information
   */
  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo;
    String checkstyleStart = "[INFO] Starting audit...";
    String checkstyleEnd = "Audit done.";

    checkstyleInfo = consoleText.substring(consoleText.indexOf(checkstyleStart),
        consoleText.indexOf(checkstyleEnd)) + checkstyleEnd;

    return checkstyleInfo;
  }

}
