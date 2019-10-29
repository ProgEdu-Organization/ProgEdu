package fcu.selab.progedu.status;

public class WebStylelistFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "> stylelint";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,end - start);
    checkstyleInfo = checkstyleInfo.replaceAll("[\n][ ]", "\n L");

    return checkstyleInfo.trim();
  }
}
