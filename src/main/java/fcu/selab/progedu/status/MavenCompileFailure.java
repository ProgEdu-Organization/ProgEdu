package fcu.selab.progedu.status;

public class MavenCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "[ERROR] COMPILATION ERROR :";
    String feedbackEnd = "[INFO] BUILD FAILURE";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
        consoleText.indexOf(feedbackEnd) + 20);
    return feedback;
  }

}
