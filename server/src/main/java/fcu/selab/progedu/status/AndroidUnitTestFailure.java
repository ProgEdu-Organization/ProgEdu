package fcu.selab.progedu.status;

public class AndroidUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    return null;
  }
}