package fcu.selab.progedu.status;

public class AndroidUiTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "> Task :app:connectedDebugAndroidTest";
    String feedbackEnd = "> Task :app:connectedDebugAndroidTest FAILED";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }
}