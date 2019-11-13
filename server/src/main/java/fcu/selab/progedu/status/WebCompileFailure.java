package fcu.selab.progedu.status;

public class WebCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    return "WebCompileFailure";
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    return consoleText;
  }
}
