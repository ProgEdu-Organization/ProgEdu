package fcu.selab.progedu.status;

public class MavenCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "[INFO] Scanning for projects...";
    String feedbackEnd = "Build step";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
        consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }

}
