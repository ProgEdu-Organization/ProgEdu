package fcu.selab.progedu.status;

public class WebCheckstyleFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "npm run htmlhint";
    String checkstyleEnd = "Archiving artifacts";

    String checkstyleInfo = consoleText.substring(
        consoleText.indexOf(checkstyleStart) + checkstyleStart.length() + 1,
        consoleText.lastIndexOf(checkstyleEnd) - 1);
    return checkstyleInfo.trim();
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    return consoleText;
  }
}
