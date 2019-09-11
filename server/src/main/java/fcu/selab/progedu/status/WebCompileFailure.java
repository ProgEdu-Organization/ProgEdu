package fcu.selab.progedu.status;

public class WebCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    
    return "WebCompileFailure";
  }
}
