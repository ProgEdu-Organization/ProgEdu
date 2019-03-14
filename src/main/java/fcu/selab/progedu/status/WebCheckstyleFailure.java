package fcu.selab.progedu.status;

public class WebCheckstyleFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkStyleInfo;
    String checkStyleStart = "> eslint -c ./src/test/.eslintrc.js ./ & htmlhint ./";
    String checkStyleEnd = "Scanned";
    
    checkStyleInfo = consoleText.substring(consoleText.indexOf(checkStyleStart) 
        + checkStyleStart.length(), consoleText.indexOf(checkStyleEnd ));
    
    return checkStyleInfo;
  }
}
