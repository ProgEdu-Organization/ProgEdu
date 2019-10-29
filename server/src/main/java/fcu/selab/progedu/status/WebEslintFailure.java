package fcu.selab.progedu.status;

public class WebEslintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "Warning:";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    int endrow = checkstyleInfo.indexOf("\n", checkstyleInfo.indexOf("problems"));
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,endrow);
    checkstyleInfo = checkstyleInfo.replace("/var/jenkins_home/workspace/","");
    return checkstyleInfo.trim();
  }
}
