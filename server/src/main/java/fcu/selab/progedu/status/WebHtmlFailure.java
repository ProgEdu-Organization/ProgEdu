package fcu.selab.progedu.status;

public class WebHtmlFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo = "WebCheckstyleFailure";
    String checkstyleStart = "   /var/jenkins_home/workspace/";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    checkstyleInfo = consoleText.substring(start,end);
    checkstyleInfo = checkstyleInfo.replaceAll("[\u001B][\\[][\\d]{0,3}[m]", "");
    checkstyleInfo = checkstyleInfo.replaceAll("\\^", " ^");

    return checkstyleInfo.trim();
  }
}
