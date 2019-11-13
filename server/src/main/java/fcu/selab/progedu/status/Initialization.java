package fcu.selab.progedu.status;

public class Initialization implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    return "Instructor Commit";
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    return "Instructor Commit";
  }

}
