package fcu.selab.progedu.status;

public class WebEsFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo = "WebCheckstyleFailure";
    String checkstyleStart = "> eslint";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,end - start);
    checkstyleInfo = checkstyleInfo.replace("/var/jenkins_home/workspace/","");
    return checkstyleInfo.trim();
  }
}
