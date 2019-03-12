package fcu.selab.progedu.status;

public class WebCheckstyleFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo;
    String checkstyleStart = "> eslint -c ./src/test/.eslintrc.js ./ & htmlhint ./";
    String checkstyleEnd = "Scanned";
    
    checkstyleInfo = consoleText.substring(consoleText.indexOf(checkstyleStart),
        consoleText.indexOf("\n",consoleText.indexOf(checkstyleEnd)));
    return checkstyleInfo;
  }
}
