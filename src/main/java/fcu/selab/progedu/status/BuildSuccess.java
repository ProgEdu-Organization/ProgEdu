package fcu.selab.progedu.status;

public class BuildSuccess implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    return "Success";
  }

}
