package fcu.selab.progedu.status;

public class CheckstyleFailure implements Status {

  /**
   * get checkstyle information
   * 
   * @param detail console text
   * @return checkstyle information
   */
  @Override
  public String getConsole(String consoleText) {
    String checkstyleInfo;
    String checkstyleStart = "Starting audit...";
    String checkstyleEnd = "Audit done.";
    checkstyleInfo = consoleText.substring(
        consoleText.indexOf(checkstyleStart) + checkstyleStart.length(),
        consoleText.indexOf(checkstyleEnd));

    return checkstyleInfo;
  }

}
